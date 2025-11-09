<#import "template.ftl" as layout>
<@layout.layoutBody>
<form id="login-form" class="login-card" action="${url.loginAction}" method="POST" autocomplete="off" novalidate>
    <div id="phone-step" class="login-step">
        <h2 class="step-title">${msg("phoneLabel")}</h2>
        <label for="phone" class="step-description">
            ${msg("phoneIntroText")}
        </label>
        <div class="input-group">
            <input
                type="tel"
                id="phone"
                name="phone"
                placeholder="${msg('phonePlaceholder',' ')}"
                value="${phone!''}"
                aria-invalid="<#if phoneErrorKey??>true</#if>"
                autocomplete="off">
            <label for="phone">${msg("phoneLabel")}</label>
        </div>
        <#if phoneErrorKey??>
            <div class="input-error">
                <span class="error-text">${msg(phoneErrorKey)}</span>
            </div>
        </#if>
        <div class="button-group">
            <button type="submit" id="btn-send-otp" class="btn-primary">
                ${msg("sendOtpText")}
            </button>
        </div>
    </div>
</form>
<script>
document.getElementById("login-form").addEventListener("submit", function () {
    const btn = document.getElementById("btn-send-otp");
    btn.disabled = true;
    btn.innerText = "${msg('sendingOtpText')}";
});
</script>
</@layout.layoutBody>
