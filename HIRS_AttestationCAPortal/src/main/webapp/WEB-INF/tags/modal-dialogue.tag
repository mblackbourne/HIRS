<%@tag description="basic modal dialog" pageEncoding="UTF-8"%>

<%@attribute name="id"%>
<%@attribute name="label"%>
<%@attribute name="customButton" fragment="true" required="false"%>

<div id="${id}" class="modal fade" role="dialog" style="top:5%">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h1 id="modal-title">${label}</h1>
            </div>
            <div class="modal-body">
                <jsp:doBody/>
            </div>
            <div class="modal-footer">
                <div class="modal-custom-button">
                    <jsp:invoke fragment="customButton"/>
                </div>
                <button class="btn btn-secondary" data-dismiss="modal">Done</button>
            </div>
        </div>
    </div>
</div>