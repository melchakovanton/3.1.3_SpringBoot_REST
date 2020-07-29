jQuery(function ($) {

    createTable();

    $('#registrationButton').click(function (e) { //e - это объект события, которое сработало. В нашем случае - нажатие на кнопку в форме регистрации

        e.preventDefault(); // Убираем у события действие по-умолчанию - отправку данных на сервер.

        $('#ajaxRegistrationDiv').html('<h4>Registering new user...</h4>').fadeIn(1, function () {
            let createObject = {};
            createObject["name"] = $("#usernameInput").val();
            createObject["password"] = $("#passwordInput").val();
            createObject["age"] = $("#ageInput").val();
            createObject["roles"] = $("#rolesInput").val();

            $.ajax({
                url: '/rest/userAdd',
                type: 'POST',
                contentType: "application/json;charset=UTF-8",
                data: JSON.stringify(createObject), //тип данных, передаваемых на сервер
                dataType: 'json', //тип данных, ожидаемый в качестве ответа от сервера
                context: document.getElementById('ajaxRegistrationDiv'), //задает содержимое переменной this
                success: function (data) { //функция в success сработает при получении ответа от сервера с кодом 200 - ОК
                    $(this).fadeOut(500, function () {
                        $(this).toggleClass('alert-primary alert-success');
                        $(this).find('h4').attr('class', 'alert-heading').text('New user registered!');
                        $(this).append(`<hr><h5>User ${data.name}</h5><p>age: ${data.age}</p><p>roles: ${data.roles}</p>`);
                        $(this).fadeIn(1000)
                            .delay(2000)
                            .fadeOut(1000, function () {
                                $("#registrationForm").trigger("reset");
                            });
                    });
                    addTableRow(data);
                },
                error: function () {
                    alert("Error!");
                }
            });
        })
    });

    $('#tbody').on('click', '.delete-row', function () {

        let id = this.id.slice(this.id.lastIndexOf('-') + 1);
        $('#id-input-delete').attr('value', id);
        $('#id-input-hidden-delete').attr('value', id);
        $('#username-edit-delete').attr('value', $('#username-' + id).text());
        $('#password-edit-delete').attr('value', "");
        $('#age-edit-delete').attr('value', $('#userAge-' + id).text());
        let userRow = $("[id=" + id + "]");
        let rolesList = ["admin", "user"];
        let userRoles = userRow.find('#userRoles-' + id).text();
        $('#role-edit-delete').empty();
        rolesList.forEach(function (value) {
            if (userRoles.includes(value)) {
                $('#role-edit-delete').append('<option id="option"' + value + ' value="' + value + '" selected>' + value + '</option>')
            } else {
                $('#role-edit-delete').append('<option id="option"' + value + ' value="' + value + '">' + value + '</option>')
            }
        });
    });

    //Удаление Пользователя
    $('#delete-user').on('click', function () {
        let id = $('#id-input-hidden-delete').val()
        $.ajax("/rest/delete/" + id, {
            data: {id: id},
            method: "delete",
            success: function () {
                $('#' + id).remove();
                $('#modal-delete #close-delete-btn').click();
            },
            error: function () {
                alert("Error!");
            }
        })
    })


    $('#tbody').on('click', '.edit-user', function () {

        let id = this.id.slice(this.id.lastIndexOf('-') + 1);
        $('#id-input').attr('value', id);
        $('#id-input-hidden').attr('value', id);
        $('#username-edit').attr('value', $('#username-' + id).text());
        $('#password-edit').attr('value', "");
        $('#age-edit').attr('value', $('#userAge-' + id).text());
        let userRow = $("[id=" + id + "]");
        let rolesList = ["admin", "user"];
        let userRoles = userRow.find('#userRoles-' + id).text();
        $('#role-edit').empty();
        rolesList.forEach(function (value) {
            if (userRoles.includes(value)) {
                $('#role-edit').append('<option id="option"' + value + ' value="' + value + '" selected>' + value + '</option>')
            } else {
                $('#role-edit').append('<option id="option"' + value + ' value="' + value + '">' + value + '</option>')
            }
        });

    });

    // $('#update-user').on('click', function () {
    //
    //     let updateId = $('#id-input-hidden').prop("value");
    //     let roles = [];
    //     if ($('#u_roles_admin').prop("selected")) {
    //         roles.push("admin")
    //     }
    //     if ($('#u_roles_user').prop("selected")) {
    //         roles.push("user")
    //     }
    //
    //     let updateData = {
    //         "id": updateId,
    //         "name": $('#username-edit').prop("value"),
    //         "password": $('#password-edit').prop("value"),
    //         "age": $('#age-edit').prop("value"),
    //         "roles": roles,
    //     }
    //
    //     $.ajax("/rest/editUser", {
    //         type: "PUT",
    //         data: JSON.stringify(updateData),
    //         dataType: "json",
    //         contentType: "application/json",
    //         success: function () {
    //             $.getJSON("/rest/users/" + updateId, function (data) {
    //                 updateRow(data);
    //             });
    //             $('#modal-edit #close-update-user').click();
    //
    //             // $('#updateUserModal').modal('hide');
    //         }
    //     })
    // })

    // Edit User
    $('#update-user').on('click', function () {
        let updateObject = {};
        updateObject["id"] = $("#id-input-hidden").val();
        updateObject["name"] = $("#username-edit").val();
        updateObject["password"] = $("#password-edit").val();
        updateObject["age"] = $("#age-edit").val();
        updateObject["roles"] = $("#role-edit").val();

        let json = JSON.stringify(updateObject);
        $.ajax({
            url: '/rest/editUser',
            type: 'PUT',
            contentType: "application/json;charset=UTF-8",
            data: json, //тип данных, передаваемых на сервер
            dataType: 'json', //тип данных, ожидаемый в качестве ответа от сервера

            success: function (incdata) {

                $(`#${incdata.id}`).replaceWith(`
                    <tr id="${incdata.id}">
                        <td id="userId-${incdata.id}">${incdata.id}</td>
                        <td id="username-${incdata.id}">${incdata.name}</td>
                        <td id="password-${incdata.id}">${incdata.password}</td>
                        <td id="userAge-${incdata.id}">${incdata.age}</td>
                        <td id="userRoles-${incdata.id}">${incdata.roles}</td>
                        <td><button type="button" class="btn btn-info edit-user" data-toggle="modal" data-target="#modal-edit" id="editButton-${incdata.id}">Edit</button></td>
                        <td><button type="button" class="btn btn-danger delete-row" data-toggle="modal" data-target="#modal-delete" id="deleteButton-${incdata.id}">Delete</button></td>
                    </tr>
                `)
                $('#modal-edit .close-btn').click();

            },
            error: function () {
                alert("Error!");
            }
        })
    })
});

function updateRow(data) {
    //
    // let editButton = document.querySelector("editButton-${id}");
    // let deleteButton = document.querySelector("#deleteButton-${id}");

    let tableRow = $('#' + data.id)
    tableRow.empty();
    tableRow = tableRow[0];

    let cellId = tableRow.insertCell();
    cellId.innerHTML = data.id;

    let cellFirstName = tableRow.insertCell();
    cellFirstName.innerHTML = data.name;

    let cellPassword = tableRow.insertCell();
    cellPassword.innerHTML = data.password;

    let cellAge = tableRow.insertCell();
    cellAge.innerHTML = data.age;

    let cellRoles = tableRow.insertCell();
    let roles = "";
    data.roles.forEach((v) => {
        roles += v.name + " ";
    });
    cellRoles.innerHTML = roles;

    // let cellEditButton = tableRow.insertCell();
    // let cloneEdit = editButton.cloneNode(true);
    // cloneEdit.value = data.id;
    // cloneEdit.removeAttribute("hidden");
    // cellEditButton.appendChild(cloneEdit);
    //
    // let cellDeleteButton = tableRow.insertCell();
    // let cloneDelete = deleteButton.cloneNode(true);
    // cloneDelete.value = data.id;
    // cloneDelete.removeAttribute("hidden");
    // cellDeleteButton.appendChild(cloneDelete);

}

function createTable() {

    $('#tbody').empty();

    $.ajax({
        url: '/rest',
        type: 'GET',
        contentType: "application/json;charset=UTF-8",
        dataType: 'json', //тип данных, ожидаемый в качестве ответа от сервера
        success: function (data) { //функция в success сработает при получении ответа от сервера с кодом 200 - ОК
            data.forEach(function (element) {
                addTableRow(element);
            })
        },
    });
}

function addTableRow(element) {
    let id = element.id;
    let name = element.name;
    let password = element.password;
    let age = element.age;
    let roles = element.stringRole;
    let markup = `<tr id="${id}">
                        <td id="userId-${id}">${id}</td>
                        <td id="username-${id}">${name}</td>
                        <td id="password-${id}">${password}</td>
                        <td id="userAge-${id}">${age}</td>
                        <td id="userRoles-${id}">${roles}</td>
                        <td><button type="button" class="btn btn-info edit-user" data-toggle="modal" data-target="#modal-edit" id="editButton-${id}">Edit</button></td>
                        <td><button type="button" class="btn btn-danger delete-row" data-toggle="modal" data-target="#modal-delete" id="deleteButton-${id}">Delete</button></td>
                  </tr>`;
    $('#tbody').append(markup);
}
