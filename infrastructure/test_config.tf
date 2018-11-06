data "azurerm_key_vault_secret" "source_test_s2s_secret" {
  name      = "microservicekey-jui-webapp"
  vault_uri = "${local.s2s_vault_url}"
}
