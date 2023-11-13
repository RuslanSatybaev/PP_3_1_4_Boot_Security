$(async function () {
    await getRegisUser();
});

const userFetchService = {
    findRegis: async () => await fetch('api/rgs'),
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
            $('#panelBody').append(user);
        })
        .catch(function (err) {
            console.log('error: ' + err);
        });
}
