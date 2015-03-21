<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <title>Edit Movie</title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><g:link class="list" action="list">Movie List</g:link></span>
            <span class="menuButton"><g:link class="create" action="create">New Movie</g:link></span>
        </div>
        <div class="body">
            <h1>Edit Movie</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${movieInstance}">
            <div class="errors">
                <g:renderErrors bean="${movieInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <input type="hidden" name="id" value="${movieInstance?.id}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="title">Title:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:movieInstance,field:'title','errors')}">
                                    <input type="text" id="title" name="title" value="${fieldValue(bean:movieInstance,field:'title')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="director">Director:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:movieInstance,field:'director','errors')}">
                                    <input type="text" id="director" name="director" value="${fieldValue(bean:movieInstance,field:'director')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="year">Year:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:movieInstance,field:'year','errors')}">
                                    <input type="text" id="year" name="year" value="${fieldValue(bean:movieInstance,field:'year')}" />
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="price">Price:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:movieInstance,field:'price','errors')}">
                                    <input type="text" id="price" name="price" value="${fieldValue(bean:movieInstance,field:'price')}" />
                                </td>
                            </tr> 
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" value="Update" /></span>
                    <span class="button"><g:actionSubmit class="delete" onclick="return confirm('Are you sure?');" value="Delete" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
