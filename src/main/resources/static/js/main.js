$(async function () {
    await getTableWithUsers();
    await getRegisUser();
    await newUser();
    updateUser();
    removeUser();
});

const userFetchService = {
    head: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Referer': null
    },
    findAllUsers: async () => await fetch('api/all'),
    findRoles: async () => await fetch('api/allRoles'),
    findRegis: async () => await fetch('api/rgs'),
    findOneUser: async (id) => await fetch(`api/${id}`),
    addNewUser: async (user) => await fetch('api/', {
        method: 'POST',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    updateUser: async (user, id) => await fetch(`api/updateUser/${id}`, {
        method: 'PATCH',
        headers: userFetchService.head,
        body: JSON.stringify(user)
    }),
    deleteUser: async (id) => await fetch(`api/deleteUser/${id}`, {method: 'DELETE', headers: userFetchService.head})
}

const table = $('#tbodyAllUserTable');

async function getTableWithUsers() {
    table.empty()
    userFetchService.findAllUsers()
        .then(res => res.json())
        .then(data => {
            data.forEach(user => {
                let tableWithUsers = `$(
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.firstName}</td>
                            <td>${user.lastName}</td>
                            <td>${user.age}</td>                                                 
                            <td>${user.email}</td>
                            <td>${user.roleList.map(role => " " + role.roleName.substring(5))}</td>
                            <td>
                                <button type="button" class="btn btn-info" data-toggle="modal" id="buttonEdit"
                                data-action="edit" data-id="${user.id}" data-target="#edit">Edit</button>
                            </td>
                            <td>
                                <button type="button" class="btn btn-danger" data-toggle="modal" id="buttonDelete"
                                data-action="delete" data-id="${user.id}" data-target="#delete">Delete</button>
                            </td>
                        </tr>)`;
                table.append(tableWithUsers);
            })
        })
}

function appendData(user) {
    let mainContainer = document.getElementById("regis_user");
    let div = document.createElement("div");
    div.innerHTML = user.email + ' with roles: ' + user.rolesToString;
    mainContainer.appendChild(div);
}

async function getRegisUser() {
    await userFetchService.findRegis()
        .then(res => res.json())
        .then(function (user) {
            appendData(user);
            user = `$(
            <tr>
                <td>${user.id}</td>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.rolesToString}</td>
                )`;
            $('#userPanelBody').append(user);
            $('#panelBody').append(user);
        })
        .catch(function (err) {
            console.log('error: ' + err);
        });
}

// Add new user
async function newUser() {
    await userFetchService.findRoles()
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let el = document.createElement("option");
                el.text = role.roleName.substring(5);
                el.value = role.id;
                el.name = role.roleName;
                $('#newUserRoles')[0].appendChild(el);
            })
        })

    const form = document.forms["formNewUser"];

    form.addEventListener('submit', addNewUser)

    async function addNewUser(e) {
        e.preventDefault();
        let newUserRoles = [];
        for (let i = 0; i < form.roles.options.length; i++) {
            if (form.roles.options[i].selected) {
                newUserRoles.push({
                    id: form.roles.options[i].value,
                    name: form.roles.options[i].name
                })
            }
        }

        const user = {
            firstName: form.firstName.value,
            lastName: form.lastName.value,
            age: form.age.value,
            email: form.email.value,
            password: form.password.value,
            roleList: newUserRoles
        };

        const response = await userFetchService.addNewUser(user);
        if (response.ok) {
            form.reset();
            await getTableWithUsers();
            $('#allUsersTable').click();
        } else {
            let error = await response.json();
            console.log(error)
        }
    }
}

// Update user
function updateUser() {
    const editForm = document.forms["formEditUser"];
    editForm.addEventListener("submit", async ev => {
        ev.preventDefault();
        let editUserRoles = [];
        for (let i = 0; i < editForm.roles.options.length; i++) {
            if (editForm.roles.options[i].selected) {
                editUserRoles.push({
                    id: editForm.roles.options[i].value,
                    roleName: "ROLE_" + editForm.roles.options[i].text
                })
            }
        }

        const userToUpdate = {
            id: editForm.id.value,
            firstName: editForm.firstName.value,
            lastName: editForm.lastName.value,
            age: editForm.age.value,
            email: editForm.email.value,
            password: editForm.password.value,
            roleList: editUserRoles
        };

        const response = await userFetchService.updateUser(userToUpdate, editForm.id.value);
        if (response.ok) {
            $('#editFormCloseButton').click();
            getTableWithUsers();
        } else {
            let error = await response.json;
            console.log(error);
        }
    })
}

$('#edit').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showEditModal(id);
})

async function showEditModal(id) {
    $('#rolesEditUser').empty();
    let user = await getUser(id);
    let form = document.forms["formEditUser"];
    form.id.value = user.id;
    form.firstName.value = user.firstName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.email.value = user.email;
    form.password.value = '';

    await userFetchService.findRoles()
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roleList.length; i++) {
                    if (user.roleList[i].roleName === role.roleName) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.roleName.substring(5);
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $('#rolesEditUser')[0].appendChild(el);
            })
        })
}

// Delete user
function removeUser() {
    const deleteForm = document.forms["formDeleteUser"];
    deleteForm.addEventListener("submit", ev => {
        ev.preventDefault();
        userFetchService.deleteUser(deleteForm.id.value)
            .then(() => {
                $('#deleteFormCloseButton').click();
                getTableWithUsers();
            })
    })
}

$('#delete').on('show.bs.modal', ev => {
    let button = $(ev.relatedTarget);
    let id = button.data('id');
    showDeleteModal(id);
})

async function showDeleteModal(id) {
    let user = await getUser(id);
    let form = document.forms["formDeleteUser"];
    form.id.value = user.id;
    form.firstName.value = user.firstName;
    form.lastName.value = user.lastName;
    form.age.value = user.age;
    form.email.value = user.email;

    $('#rolesDeleteUser').empty();

    await userFetchService.findRoles()
        .then(res => res.json())
        .then(roles => {
            roles.forEach(role => {
                let selectedRole = false;
                for (let i = 0; i < user.roleList.length; i++) {
                    if (user.roleList[i].roleName === role.roleName) {
                        selectedRole = true;
                        break;
                    }
                }
                let el = document.createElement("option");
                el.text = role.roleName.substring(5);
                el.value = role.id;
                if (selectedRole) el.selected = true;
                $('#rolesDeleteUser')[0].appendChild(el);
            })
        });
}

async function getUser(id) {
    let response = await userFetchService.findOneUser(id);
    return await response.json();
}
