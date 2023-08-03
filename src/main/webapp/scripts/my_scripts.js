function editTask(id) {
    let identifier_delete = "#button_delete_" + id;
    let identifier_edit = "#button_edit_" + id;

    $(identifier_delete).remove(identifier_delete);

    let table_row = $(identifier_edit).parent().parent();
    let children = table_row.children();

    let table_data_description = children[1];
    table_data_description.innerHTML = "<input id='input_description_" + id + "' value='" + table_data_description.innerHTML + "'>";

    let table_data_status = children[2];
    let current_status = table_data_status.innerHTML;
    let status_id = "#select_status" + id;
    table_data_status.innerHTML = getDropDownStatusList(id);
    $(status_id).val(current_status).change();

    $(identifier_edit).html('save').attr("onclick", "saveTask(" + id + ")");
}

function saveTask(id) {
    let taskDescription = $("#input_description_" + id).val();
    let taskStatus = $("#select_status" + id).val();

    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        url: getBaseURL() + id,
        async: false,
        data: JSON.stringify({"description": taskDescription, "status": taskStatus})
    });

    window.document.location.reload();
}

function deleteTask(id) {
    $.ajax({
        type: "DELETE",
        url: getBaseURL() + id,
        async: false,
        success: function () {
            window.location.reload();
        }
    });
}

function createTask(lastPage, tasksCount, limit) {
    let taskDescription = $("#create_description_input").val();
    let taskStatus = $("#create_task_status").val();

    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        url: getBaseURL(),
        async: false,
        data: JSON.stringify({"description": taskDescription, "status": taskStatus})
    })

    if (tasksCount % limit === 0) {
        window.document.location.assign(getBaseURL() + "?page=" + (lastPage + 1) + "&limit=" + limit);
    } else window.document.location.assign(getBaseURL() + "?page=" + lastPage  + "&limit=" + limit);
}

function getDropDownStatusList(id) {
    let status_id = "select_status" + id;
    return "<select id =" + status_id + ">" +
        "<option value='IN_PROGRESS'>IN_PROGRESS</option>" +
        "<option value='DONE'>DONE</option>" +
        "<option value='PAUSED'>PAUSED</option>" +
        "</select>"
}

function showTasksList() {
    window.document.location.assign(getBaseURL() + "?page=" + 1 + "&limit=" + document.getElementById("tasks_per_page").value);
}

function getBaseURL() {
    let current_path = window.location.href;
    let end_position = current_path.indexOf("?");
    return current_path.substring(0, end_position);
}