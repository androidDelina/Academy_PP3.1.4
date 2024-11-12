console.log('Скрипт подключен и работает');
const url = 'http://localhost:8080/admin/api/users/';
const urlRoles = 'http://localhost:8080/admin/api/roles/';
const container = document.querySelector('.users_table_body');
const newUserForm = document.getElementById('newUserForm');
const editUserForm = document.getElementById('editUserForm');
const deleteUserForm = document.getElementById('deleteUserForm');
const btnCreate = document.getElementById('new-user-tab');
let result = '';

var editUserModal = new bootstrap.Modal(document.getElementById('editUserModal'));
var deleteUserModal = new bootstrap.Modal(document.getElementById('deleteUserModal'));

const editId = document.getElementById('editId');
const editName = document.getElementById('editFirstName');
const editSurname = document.getElementById('editLastName');
const editUsername = document.getElementById('editUsername');
const editPassword = document.getElementById('editPassword');
const editCity = document.getElementById('editCity');
const editRoles = document.getElementById('editRoles');

const delId = document.getElementById('idDelete');
const delName = document.getElementById('nameDelete');
const delSurname = document.getElementById('surnameDelete');
const delUsername = document.getElementById('usernameDelete');
const delPassword = document.getElementById('deletePassword');
const delCity = document.getElementById('deleteCity');
const delRoles = document.getElementById('deleteRoles');

const newName = document.getElementById('newFirstName');
const newSurname = document.getElementById('newLastName');
const newUsername = document.getElementById('newUsername');
const newPassword = document.getElementById('newPassword');
const newCity = document.getElementById('newCity');
const newRoles = document.getElementById('newRoles');


let rolesArr = [];
let userRolesArr = []
let usersArr = [];

const getAllUsers = (users) => {

    usersArr = [];
    users.forEach(user => {
        usersArr.push(user);
        let roles = user.roles.map(role => role.role).join(' ');
        result += `
        <tr>
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>                
            <td>${user.username}</td>
            <td>${roles}</td>
            <td>${user.sex}</td>
            <td>${user.city}</td>
            <td><a class="btnEdit btn btn-sm btn-info text-white">Edit</a></td>
            <td><a class="btnDelete btn btn-danger btn-sm">Delete</a></td>
        </tr>
    `;
    });
    container.innerHTML = result;
};

const getRoles = (roles) => {
    let rolesOptions = '';
    roles.forEach(role => {
        rolesOptions += `
            <option value="${role.id}">${role.role}</option>
        `
        rolesArr.push(role);
    })
    newRoles.innerHTML = rolesOptions;
    editRoles.innerHTML = rolesOptions;

    delRoles.innerHTML = rolesOptions;
}

fetch(url)
    .then(res => res.json())
    .then(data => getAllUsers(data))
    .catch(error => console.log(error));

var allRoles;

fetch(urlRoles)
    .then(res => res.json())
    .then(data => {
        allRoles = data;
        getRoles(allRoles)
    });

const refreshListOfUsers = () => {
    fetch(url)
        .then(res => res.json())
        .then(data => {
            result = '';
            getAllUsers(data)
        })
}

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e)
        }
    })
}


const cities = ["Moscow", "Saint Petersburg"];
on(document, 'click', '.btnDelete', e => {
    const row = e.target.parentNode.parentNode;
    idForm = row.children[0].innerHTML;
    const firstNameForm = row.children[1].innerHTML;
    const lastNameForm = row.children[2].innerHTML;
    const usernameForm = row.children[3].innerHTML;
    const passwordForm = row.children[4].innerHTML;
    const sexForm = row.children[5].innerHTML.trim();
    const cityForm = row.children[6].innerHTML.trim();
    const userRoles = row.children[7].innerText.trim().split(', ');

    delId.value = idForm;
    delName.value = firstNameForm;
    delSurname.value = lastNameForm;
    delUsername.value = usernameForm;
    delPassword.value = ''

    // Установка значения пола
    document.querySelector('#maleDelete').checked = (sexForm === 'male');
    document.querySelector('#femaleDelete').checked = (sexForm === 'female')

    delCity.innerHTML = ''; // Очищаем список перед добавлением городов
    cities.forEach(city => {
        const option = document.createElement('option');
        option.value = city;
        option.textContent = city;
        if (city === cityForm) {
            option.selected = true; // Устанавливаем выбранный город
        }
        delCity.appendChild(option);
    });

    let rolesOptions = '';
    console.log(usersArr)
    console.log(idForm);
    let userRoless = usersArr.find(user => user.id == idForm)?.roles || [];

    console.log(userRoless);
    if (userRoless) {
        userRoless.forEach(role => {
            // const option = document.createElement('option');
            // option.value = role.id;
            // option.textContent = role.value;
            rolesOptions += `
            <option value="${role.id}">${role.role}</option>
        `
            userRolesArr.push(role);
        })
    } else {
        console.warn(`Roles not found for user at id: ${idForm}`);
    }
    console.log('Роли пользователя:', userRoless);
    delRoles.innerHTML = rolesOptions;

    console.log(delRoles)
    deleteUserModal.show();
})


let idForm = 0;
on(document, 'click', '.btnEdit', e => {
    console.log('Открыто окно редактирования');

    const row = e.target.parentNode.parentNode;
    idForm = row.children[0].innerHTML;
    const firstNameForm = row.children[1].innerHTML;
    const lastNameForm = row.children[2].innerHTML;
    const usernameForm = row.children[3].innerHTML;
    const passwordForm = row.children[4].innerHTML;
    const sexForm = row.children[5].innerHTML.trim();
    const cityForm = row.children[6].innerHTML.trim();

    editId.value = idForm;
    editName.value = firstNameForm;
    editSurname.value = lastNameForm;
    editUsername.value = usernameForm;
    editPassword.value = ''

    // Установка значения пола
    document.querySelector('#maleUpdate').checked = (sexForm === 'male');
    document.querySelector('#femaleUpdate').checked = (sexForm === 'female');

    const citySelect = document.getElementById('editCity');
    citySelect.innerHTML = ''; // Очищаем список перед добавлением городов
    cities.forEach(city => {
        const option = document.createElement('option');
        option.value = city;
        option.textContent = city;
        if (city === cityForm) {
            option.selected = true; // Устанавливаем выбранный город
        }
        citySelect.appendChild(option);
    });
    editRoles.options.selectedIndex = -1;
    editUserModal.show();
    console.log(editUserModal);
})


btnCreate.addEventListener('click', () => {
    newName.value = '';
    newSurname.value = '';
    newUsername.value = '';
    newPassword.value = '';
    const citySelect = document.getElementById('newCity');
    citySelect.innerHTML = ''; // Очищаем список перед добавлением городов
    cities.forEach(city => {
        const option = document.createElement('option');
        option.value = city;
        option.textContent = city;
        citySelect.appendChild(option);
    });
    newRoles.options.selectedIndex = -1;
});

deleteUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    fetch(url + delId.value, {
        method: 'DELETE',
        headers: {
            'Content-type': 'application/json; charset=UTF-8'
        },
    })
        .then(res => res.json())
        .catch(err => console.log(err))
        .then(refreshListOfUsers);
    deleteUserModal.hide();
});

newUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let rolesJ = [];
    const selectedOpts = [...newRoles.options]
        .filter(x => x.selected)
        .map(x => x.value);

    selectedOpts.forEach(
        role => {
            rolesJ.push(rolesArr.find(r => r.id == role));
        }
    );

    const sexForm = document.querySelector('input[name="sex"]:checked').value;
    const cityForm = newCity.value;

    const fetchFunction = async () => {
        const fetchedData = await
            fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    name: newName.value,
                    surname: newSurname.value,
                    username: newUsername.value,
                    password: newPassword.value,
                    sex: sexForm,
                    city: cityForm,
                    roles: rolesJ
                })
            });

        if (!fetchedData.ok) {
            fetchedData.json()
                .then(data => alert(data.message))
        }
        return fetchedData;
    }

    fetchFunction()
        .then(response => response.json())
        .catch(err => console.log(err))
        .then(refreshListOfUsers);

    const navtab1 = document.getElementById('all-users-tab');
    const navtab2 = document.getElementById('new-user-tab');
    const tab1 = document.getElementById('all-users');
    const tab2 = document.getElementById('new-user');

    navtab1.classList.add('active');
    navtab2.classList.remove('active');
    tab1.classList.add('show', 'active');
    tab2.classList.remove('show', 'active');
})

editUserForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let rolesJ = [];
    const selectedOpts = [...editRoles.options]
        .filter(x => x.selected)
        .map(x => x.value);

    selectedOpts.forEach(
        role => {
            rolesJ.push(rolesArr.find(r => r.id == role));
        }
    );

    const sexForm = document.querySelector('input[name="sex"]:checked').value;
    const cityForm = editCity.value;

    const fetchFunction = async () => {
        console.log(url + editId.value);
        const fetchedData = await fetch(url + editId.value, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                id: editId.value,
                name: editName.value,
                surname: editSurname.value,
                username: editUsername.value,
                password: editPassword.value,
                sex: sexForm,
                city: cityForm,
                roles: rolesJ
            })
        });

        if (!fetchedData.ok) {
            const errorData = await fetchedData.json();
            alert(errorData.message);
            return; // Завершаем функцию, если произошла ошибка
        }

        return fetchedData.json();
    }

    fetchFunction()
        .then(refreshListOfUsers)
        .catch(error => console.error("Error:", error));

    editUserModal.hide();

})

const firstTabPill = document.querySelector('.nav-link.active');
const firstTab = document.querySelector('.tab-pane.fade.show.active');

window.onload = function () {
    firstTabPill.className = 'nav-link active';
    firstTab.className = 'tab-pane fade show active';
}





