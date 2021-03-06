provider "azurerm" {
  version = "1.21"
}

locals {
  app_full_name = "${var.product}-${var.component}"
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  shared_vault_name = "${var.shared_product_name}-${local.local_env}"

  previewVaultName = "${local.app_full_name}-aat"
  nonPreviewVaultName = "${local.app_full_name}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"

  nonPreviewVaultUri = "${module.cet-online-app-vault.key_vault_uri}"
  previewVaultUri = "https://cet-online-app-aat.vault.azure.net/"
  vaultUri = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultUri : local.nonPreviewVaultUri}"

  previewEnv= "aat"
  nonPreviewEnv = "${var.env}"
  localenv = "${(var.env == "preview" || var.env == "spreview") ? local.previewEnv : local.nonPreviewEnv}"

  s2s_vault_url = "https://s2s-${local.local_env}.vault.azure.net/"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"
  pdf_service_url = "http://${var.pdf_service_url}-${local.local_env}.service.${local.local_ase}.internal"
  s2s_url = "http://${var.s2s_url}-${local.local_env}.service.${local.local_ase}.internal"
  fee_api_url = "${var.fee_api_url == "" ? "http://fees-register-api-${local.local_env}.service.core-compute-${local.local_env}.internal" : var.fee_api_url}"
}

module "app" {
  source = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product = "${local.app_full_name}"
  location = "${var.location}"
  env = "${var.env}"
  ilbIp = "${var.ilbIp}"
  subscription = "${var.subscription}"
  capacity     = "${var.capacity}"
  is_frontend = false
  additional_host_name = "${local.app_full_name}-${var.env}.service.${var.env}.platform.hmcts.net"
  https_only="false"
  common_tags  = "${var.common_tags}"
  asp_rg = "${var.shared_product_name}-${var.env}"
  asp_name = "${var.shared_product_name}-${var.env}"

  app_settings = {
    POSTGRES_HOST = "${module.db.host_name}"
    POSTGRES_PORT = "${module.db.postgresql_listen_port}"
    POSTGRES_DATABASE = "${module.db.postgresql_database}"
    POSTGRES_USER = "${module.db.user_name}"
    POSTGRES_PASSWORD = "${module.db.postgresql_password}"
    MAX_ACTIVE_DB_CONNECTIONS = 70

    # JAVA_OPTS = "${var.java_opts}"
    # SERVER_PORT = "8080"

    # db
    SPRING_DATASOURCE_URL = "jdbc:postgresql://${module.db.host_name}:${module.db.postgresql_listen_port}/${module.db.postgresql_database}?ssl=true"
    SPRING_DATASOURCE_USERNAME = "${module.db.user_name}"
    SPRING_DATASOURCE_PASSWORD = "${module.db.postgresql_password}"

    ENABLE_DB_MIGRATE="false"

    # idam
    IDAM_API_URL = "${var.idam_api_url}"
    PDF_SERVICE_URL = "http://${var.pdf_service_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
    S2S_URL = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
    S2S_KEY = "${data.azurerm_key_vault_secret.s2s_key.value}"
    S2S_NAMES_WHITELIST = "${var.s2s_names_whitelist}"
    GOV_NOTIFY_API_KEY = "${data.azurerm_key_vault_secret.gov_notify_api_key.value}"

    # logging vars & healthcheck
    REFORM_SERVICE_NAME = "${local.app_full_name}"
    REFORM_TEAM = "${var.team_name}"
    REFORM_SERVICE_TYPE = "${var.app_language}"
    REFORM_ENVIRONMENT = "${var.env}"

    PACKAGES_NAME = "${local.app_full_name}"
    PACKAGES_PROJECT = "${var.team_name}"
    PACKAGES_ENVIRONMENT = "${var.env}"
    LOG_OUTPUT = "${var.log_output}"

    FORCE_CHANGE = "delete me"

    FEE_API_URL = "${local.fee_api_url}"
  }
}

module "db" {
  source = "git@github.com:hmcts/cnp-module-postgres?ref=master"
  product = "${local.app_full_name}-postgres-db"
  location = "${var.location}"
  env = "${var.env}"
  postgresql_user = "${var.postgresql_user}"
  database_name = "${var.database_name}"
  sku_name = "GP_Gen5_2"
  sku_tier = "GeneralPurpose"
  storage_mb = "51200"
  common_tags  = "${var.common_tags}"
}

data "azurerm_key_vault" "shared_key_vault" {
  name = "${local.shared_vault_name}"
  resource_group_name = "${local.shared_vault_name}"
}

data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-cet"
  vault_uri = "https://s2s-${local.localenv}.vault.azure.net/"
}

module "cet-online-app-vault" {
  source              = "git@github.com:hmcts/moj-module-key-vault?ref=master"
  name                = "${local.vaultName}"
  product             = "${var.product}"
  env                 = "${var.env}"
  tenant_id           = "${var.tenant_id}"
  object_id           = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.app.resource_group_name}"
  product_group_object_id = "ffb5f9a3-b686-4325-a26e-746db5279a42"
}

data "azurerm_key_vault_secret" "gov_notify_api_key" {
  name = "gov-notify-api-key"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name = "${local.app_full_name}-POSTGRES-USER"
  value = "${module.db.user_name}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name = "${local.app_full_name}-POSTGRES-PASS"
  value = "${module.db.postgresql_password}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name = "${local.app_full_name}-POSTGRES-HOST"
  value = "${module.db.host_name}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name = "${local.app_full_name}-POSTGRES-PORT"
  value = "${module.db.postgresql_listen_port}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name = "${local.app_full_name}-POSTGRES-DATABASE"
  value = "${module.db.postgresql_database}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "S2S_KEY" {
  name = "${data.azurerm_key_vault_secret.s2s_key.name}"
  value = "${data.azurerm_key_vault_secret.s2s_key.value}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "GOV_NOTIFY_API_KEY" {
  name = "${data.azurerm_key_vault_secret.gov_notify_api_key.name}"
  value = "${data.azurerm_key_vault_secret.gov_notify_api_key.value}"
  vault_uri = "${module.cet-online-app-vault.key_vault_uri}"
}
