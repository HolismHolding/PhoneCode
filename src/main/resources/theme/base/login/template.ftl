<#macro layoutBody>
<!DOCTYPE html>
<#if locale?? && locale.currentLanguageTag??>
<html lang="${locale.currentLanguageTag}">
<#else>
<html lang="en">
</#if>
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title>${msg("loginTitle", realm.displayName)!realm.displayName}</title>
    <link rel="icon" href="${url.resourcesPath}/img/temp.png" />
    <link rel="stylesheet" type="text/css" href="${url.resourcesPath}/css/styles.css" />
</head>
<body class="${properties.kcBodyClass!}">
    <div class="login-container">
        <header class="login-header">
            <#if logoUrl?? && logoUrl?has_content>
            <div class="login-logo">
                <span class="shine-effect"></span>
                <img src="${logoUrl}" alt="${realm.displayName!''}" />
            </div>
            </#if>
            <h1 class="header-title">${realm.displayName!''}</h1>
        </header>
        <main class="login-content">
            <#nested/>
        </main>
        <footer class="login-footer">
            <p>&copy; ${.now?string("yyyy")} ${realm.displayName!''}. ${msg("allRightsReserved",'')}</p>
        </footer>
    </div>
</body>
</html>
</#macro>
