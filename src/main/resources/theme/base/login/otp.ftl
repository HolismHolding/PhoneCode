<#import "template.ftl" as layout>
<@layout.layoutBody>
<form id="otp-form" class="login-card" action="${url.loginAction}" method="POST" autocomplete="off" novalidate>
    <input type="hidden" id="otp" name="otp" value="${otp!''}" />
    <div id="otp-step" class="login-step">
        <div class="step-header">
            <h2 class="step-title">${msg("otpLabel")}</h2>
        </div>
        <div class="step-description">
            ${msg("enterOtpInstruction")}
            <span class="phone-edit-wrapper">
                <span id="phone-display" class="phone-highlight">${phone!''}</span>
                <button type="button" id="btn-edit-phone" class="btn-icon" aria-label="${msg('changePhoneText')}">
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" d="m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L6.832 19.82a4.5 4.5 0 0 1-1.897 1.13l-2.685.8.8-2.685a4.5 4.5 0 0 1 1.13-1.897L16.863 4.487Zm0 0L19.5 7.125" />
                    </svg>
                </button>
            </span>
        </div>
        <div class="otp-group">
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
            <input type="tel" class="otp-input" maxlength="1" inputmode="numeric" autocomplete="one-time-code" />
        </div>
        <div class="button-group">
            <button type="submit" id="btn-submit-otp" class="btn-primary">
                ${msg("signInText")}
            </button>
        </div>
        <#if timer?? && timer > 0>
            <div class="resend-group">
                <button type="button" id="btn-resend" class="btn-resend" disabled>
                    ${msg("resendOtpText")} (<span id="timer-countdown">${timer}</span>)
                </button>
            </div>
        <#elseif sendingOtp?? && sendingOtp == true>
            <div class="sending-container">${msg("sendingOtpText")}</div>
        <#else>
            <div class="resend-group">
                <button type="button" id="btn-resend" class="btn-resend">
                    ${msg("resendOtpText")}
                </button>
            </div>
        </#if>
        <#if otpErrorKey??>
            <div class="input-error">
                <span class="error-text">${msg(otpErrorKey)}</span>
            </div>
        </#if>
    </div>
</form>
<script>
(function () {
    const form = document.getElementById("otp-form");
    const submitBtn = document.getElementById("btn-submit-otp");
    const hiddenOtp = document.getElementById("otp");
    const inputs = Array.from(document.querySelectorAll(".otp-input"));
    inputs.forEach((inp, idx) => {
        inp.addEventListener("input", function () {
            this.value = this.value.replace(/[^0-9]/g, "");
            if (this.value && idx < inputs.length - 1) inputs[idx + 1].focus();
        });
        inp.addEventListener("keydown", function (e) {
            if (e.key === "Backspace" && !this.value && idx > 0) inputs[idx - 1].focus();
        });
    });
    form.addEventListener("submit", function () {
        hiddenOtp.value = inputs.map(i => i.value || "").join("");
        submitBtn.disabled = true;
        submitBtn.innerText = "${msg('signingInText')}";
    });
    const timerSpan = document.getElementById("timer-countdown");
    const resendBtn = document.getElementById("btn-resend");
    if (timerSpan && resendBtn) {
        let t = parseInt(timerSpan.textContent || "0", 10) || 0;
        const iv = setInterval(() => {
            t -= 1;
            if (t <= 0) {
                clearInterval(iv);
                timerSpan.textContent = "0";
                resendBtn.disabled = false;
                return;
            }
            timerSpan.textContent = String(t);
        }, 1000);
    }
})();
function resendOtp() {}
function changePhoneNumber() {}
</script>
</@layout.layoutBody>
