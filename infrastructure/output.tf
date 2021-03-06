// used for db migrations
output "microserviceName" {
  value = "${local.app_full_name}"
}

output "vaultUri" {
  value = "${local.vaultUri}"
}

output "vaultName" {
  value = "${local.vaultName}"
}

output "idam_api_url" {
  value = "${var.idam_api_url}"
}

output "pdf_service_url" {
  value = "http://${var.pdf_service_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
}

output "s2s_url" {
  value = "http://${var.s2s_url}-${local.local_env}.service.core-compute-${local.local_env}.internal"
}

output "spring_datasource_url" {
  value = "jdbc:postgresql://${module.db.host_name}:${module.db.postgresql_listen_port}/${module.db.postgresql_database}?ssl=true"
}

output "spring_datasource_username" {
  value = "${module.db.user_name}"
}

output "spring_datasource_password" {
  value = "${module.db.postgresql_password}"
}

output "test_s2s_secret" {
  value = "${data.azurerm_key_vault_secret.source_test_s2s_secret.value}"
}

output "fee_register_api_url" {
  value = "http://fees-register-api-${local.local_env}.service.core-compute-${local.local_env}.internal"
}