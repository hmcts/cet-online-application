locals {
  app_full_name = "${var.product}-${var.component}"
  ase_name = "${data.terraform_remote_state.core_apps_compute.ase_name[0]}"
  local_env = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "aat" : "saat" : var.env}"
  shared_vault_name = "${var.shared_product_name}-${local.local_env}"

  previewVaultName = "${local.app_full_name}-aat"
  nonPreviewVaultName = "${local.app_full_name}-${var.env}"
  vaultName = "${(var.env == "preview" || var.env == "spreview") ? local.previewVaultName : local.nonPreviewVaultName}"
  #vaultUri = "${data.azurerm_key_vault.cet_key_vault.vault_uri}"
  s2s_vault_url = "https://s2s-${local.local_env}.vault.azure.net/"
  local_ase = "${(var.env == "preview" || var.env == "spreview") ? (var.env == "preview" ) ? "core-compute-aat" : "core-compute-saat" : local.ase_name}"
  s2s_url = "http://${var.s2s_url}-${local.local_env}.service.${local.local_ase}.internal"
}
# "${local.ase_name}"
# "${local.app_full_name}"
# "${local.local_env}"

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
    S2S_URL = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"

    S2S_KEY = "${data.azurerm_key_vault_secret.s2s_key.value}"
    S2S_NAMES_WHITELIST = "${var.s2s_names_whitelist}"

    # logging vars & healthcheck
    REFORM_SERVICE_NAME = "${local.app_full_name}"
    REFORM_TEAM = "${var.team_name}"
    REFORM_SERVICE_TYPE = "${var.app_language}"
    REFORM_ENVIRONMENT = "${var.env}"

    PACKAGES_NAME = "${local.app_full_name}"
    PACKAGES_PROJECT = "${var.team_name}"
    PACKAGES_ENVIRONMENT = "${var.env}"

    ROOT_APPENDER = "${var.root_appender}"
    JSON_CONSOLE_PRETTY_PRINT = "${var.json_console_pretty_print}"
    LOG_OUTPUT = "${var.log_output}"

    FORCE_CHANGE = "delete me"
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
data "azurerm_key_vault_secret" "s2s_key" {
  name      = "microservicekey-cet"
  vault_uri = "https://s2s-${var.env}.vault.azure.net/"
}

data "azurerm_key_vault" "cet_key_vault" {
  name = "${local.vaultName}"
  resource_group_name = "${module.app.resource_group_name}"
}
//
//data "azurerm_key_vault_secret" "s2s_secret" {
//  name = "cet-s2s-token"
//  vault_uri = "${data.azurerm_key_vault.cet_key_vault.vault_uri}"
//}

//data "azurerm_key_vault_secret" "oauth2_secret" {
//  name = "cet-oauth2-token"
//  vault_uri = "${data.azurerm_key_vault.shared_key_vault.vault_uri}"
//}
//

variable "team_contact" {
  default     = "#cet"
  description = "Slack channel team can be reached on for support"
}

locals {
  tags = "${merge(
    var.common_tags,
    map(
      "Team Contact", var.team_contact
    )
  )}"
}
resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.env}"
  location = "${var.location}"

  tags = "${local.tags}"
}

module "local_key_vault" {
  source = "git@github.com:hmcts/cnp-module-key-vault?ref=master"
  product = "${local.app_full_name}"
  env = "${var.env}"
  tenant_id = "${var.tenant_id}"
  object_id = "${var.jenkins_AAD_objectId}"
  resource_group_name = "${module.app.resource_group_name}"
  product_group_object_id = "a371698d-8442-4d06-a6d8-f229cc448d3e"
}


resource "azurerm_key_vault_secret" "POSTGRES-USER" {
  name = "${local.app_full_name}-POSTGRES-USER"
  value = "${module.db.user_name}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES-PASS" {
  name = "${local.app_full_name}-POSTGRES-PASS"
  value = "${module.db.postgresql_password}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_HOST" {
  name = "${local.app_full_name}-POSTGRES-HOST"
  value = "${module.db.host_name}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_PORT" {
  name = "${local.app_full_name}-POSTGRES-PORT"
  value = "${module.db.postgresql_listen_port}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "POSTGRES_DATABASE" {
  name = "${local.app_full_name}-POSTGRES-DATABASE"
  value = "${module.db.postgresql_database}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}

resource "azurerm_key_vault_secret" "S2S_KEY" {
  name = "${data.azurerm_key_vault_secret.s2s_key.name}"
  value = "${data.azurerm_key_vault_secret.s2s_key.value}"
  vault_uri = "${module.local_key_vault.key_vault_uri}"
}
