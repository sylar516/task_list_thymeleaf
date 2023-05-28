<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Лист задач</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/my.css">
</head>
<body onload="showTaskList(0)">
<table id="tasks">
    <tr>
        <th>ID</th>
        <th>Description</th>
        <th>Status</th>
    </tr>
</table>
<table id="add_task">
    <tr>
        <th colspan="3">Add new task:</th>
    </tr>
    <tr>
        <td>-</td>
        <td><input id="description_input" type="text"></td>
        <td>
            <select id="task_status">
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="DONE">DONE</option>
                <option value="PAUSED">PAUSED</option>
            </select>
        </td>
        <td><input type="button" value="save" onclick="createAccount()"></td>
    </tr>
</table>
<div id="paging_buttons">
    <label>Pages:</label>
</div>

<script>
    function showTaskList(pageNumber) {
        $("#tasks tr:has(td)").remove();
        $.ajax({
            type: "GET",
            url: "/spring_jr/rest/tasks?pageNumber=" + pageNumber + "&pageSize=" + 10,
            success: function (response) {
                $.each(response, function (i, item) {
                    $("<tr>").html("<td>"
                        + item.id + "</td><td>"
                        + item.description + "</td><td>"
                        + item.status + "</td><td>"
                        + "<button id='button_edit_" + item.id + "' onclick='editAccount(" + item.id + ")'>edit</button></td><td>"
                        + "<button id='button_delete_" + item.id + "' onclick='deleteAccount(" + item.id + ")'>delete</button></td>"
                    ).appendTo("#tasks");
                });
            }
        })

        let countPages = Math.ceil(getTasksCount() / 10);
        $("#paging_buttons button").remove();
        for (let i = 0; i < countPages; i++) {
            let button_tag = "<button>" + (i + 1) + "</button>";
            let btn = $(button_tag)
                .attr('id', "button" + i)
                .attr('onclick', "showTaskList(" + i + ")");
            $("#paging_buttons").append(btn);

            $("#button" + pageNumber).css('color', 'red');
        }
    }

    function getTasksCount() {
        let count;
        $.ajax({
            type: "GET",
            url: "/spring_jr/rest/tasks/count",
            async: false,
            success: function (response) {
                count = response;
            }
        })
        return count;
    }

    function createAccount() {
        let description = $("#description_input").val();
        let status = $("#task_status").val();

        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            url: "/spring_jr/rest/tasks",
            async: false,
            data: JSON.stringify({
                "description": description,
                "status": status
            }),
            success: function () {
                showTaskList(getLastPage());
                $("#description_input").val("");
                $("#task_status").val(status);
            }
        });
    }

    function editAccount(id) {
        let identifier_delete = "#button_delete_" + id;
        let identifier_edit = "#button_edit_" + id;

        $(identifier_delete).remove(identifier_delete);

        let save_tag = 'save';
        $(identifier_edit).html(save_tag);

        let trElem = $(identifier_edit).parent().parent();
        let children = trElem.children();

        let tdDescription = children[1];
        tdDescription.innerHTML = "<input id='input_description_" + id + "' value='" + tdDescription.innerHTML + "'>";

        let tdStatus = children[2];
        let currentStatus = tdStatus.innerHTML;
        let statusId = "#selectStatus" + id;
        tdStatus.innerHTML = getDropDownStatusList(id);
        $(statusId).val(currentStatus).change();

        $(identifier_edit).attr('onclick', "saveTask(" + id + ")");
    }

    function saveTask(id) {
        let taskDescription = $("#input_description_" + id).val();
        let taskStatus = $("#selectStatus" + id).val();

        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json;charset=UTF-8",
            url: "/spring_jr/rest/tasks/" + id,
            async: false,
            data: JSON.stringify({
                "description": taskDescription,
                "status": taskStatus
            }),
            success: function () {
                showTaskList(getCurrentPage());
            }
        });
    }

    function deleteAccount(id) {
        $.ajax({
            type: "DELETE",
            url: "/spring_jr/rest/tasks/" + id,
            success: function () {
                showTaskList(getCurrentPage());
            }
        })
    }

    function getDropDownStatusList(id) {
        let statusId = "selectStatus" + id;
        return "<select id =" + statusId + ">" +
            "<option value='IN_PROGRESS'>IN_PROGRESS</option>" +
            "<option value='DONE'>DONE</option>" +
            "<option value='PAUSED'>PAUSED</option>" +
            "</select>"
    }

    function getCurrentPage() {
        let currentPage = 1;
        $("#paging_buttons button").each(function () {
            if ($(this).css('color') === 'rgb(255, 0, 0)') {
                currentPage = $(this).text() - 1;
            }
        });
        return currentPage;
    }

    function getLastPage() {
        return Math.ceil(getTasksCount() / 10) - 1;
    }
</script>
</body>
</html>
