=== ${tag.tagName?cap_first}

${tag.description}

<#if renderer??>
image::${tag.tagName}.jpg[]
</#if>

[cols="1,2", options="header"]
.Info
|===
| Key | Value

| Tag
| ${tag.tagName}

<#if component??>
    | Component Class
    | ${component.componentClass}

    | Component Type
    | ${component.componentType}
</#if>

<#if renderer??>
    | Component Family
    | ${renderer.componentFamily}

    | Renderer Type
    | ${renderer.rendererType}

    | Renderer Class
    | ${renderer.rendererClass}
</#if>

<#if behavior??>
    | Behavior Id
    | ${behavior.behaviorId}

    | Behavior Class
    | ${behavior.handlerClass}
</#if>

<#if tag.handlerClass??>
    | Handler Class
    | ${tag.handlerClass}
</#if>
|===

[cols="1,1,1,2", options="header"]
.Attributes
|===
| Name | Type | Default | Description

<#list tag.attribute as attr>
    | ${attr.name}
    | ${attr.type}
    | ${(attr.defaultValue)!"null"}
    | ${attr.description}
</#list>
|===

==== Getting started with ${tag.tagName?cap_first}

${content}

<#if clientApi?? && clientApi.functions?has_content && clientApi.widget??>
==== Client API
===== Widget: ${clientApi.widget}
|===
| Function | Params | Return Type | Description

<#list clientApi.functions as func>
    | ${func.signature}
    |
    <#list func.params as param>
        ${param.name} - ${param.description}
        <#sep>+
    <#else>
        -
    </#list>
    | ${func.returns.type}
    | ${func.description}
</#list>
|===
</#if>