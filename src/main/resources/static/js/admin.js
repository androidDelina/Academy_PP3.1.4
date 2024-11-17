let serverUrl = '';
let usersApiUrl = '';
let rolesApiUrl = '';

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
const delCity = document.getElementById('cityDelete');
const delRoles = document.getElementById('rolesDelete');

const newName = document.getElementById('newFirstName');
const newSurname = document.getElementById('newLastName');
const newUsername = document.getElementById('newUsername');
const newPassword = document.getElementById('newPassword');
const newCity = document.getElementById('newCity');
const newRoles = document.getElementById('newRoles');

let rolesArr = [];
let userRolesArr = [];
let usersArr = [];


const initializeConfig = async () => {
    try {
        const response = await fetch('/api/config');
        if (!response.ok) throw new Error('Ошибка при загрузке конфигурации');

        const config = await response.json();
        serverUrl = `${config.serverUrl}:${config.serverPort}`;
        usersApiUrl = `${serverUrl}/admin/api/users/`;
        rolesApiUrl = `${serverUrl}/admin/api/roles/`;


        console.log(`Config loaded: ${serverUrl}`);
        initializeApp();
    } catch (error) {
        console.error('Ошибка при загрузке конфигурации:', error);
    }
};

const initializeApp = () => {
    fetch(usersApiUrl)
        .then(res => {
            if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
            return res.json();
        })
        .then(data => {
            if (!Array.isArray(data)) throw new Error('Users data is not an array');
            getAllUsers(data);
        })
        .catch(error => console.log('Ошибка при загрузке пользователей:', error));

    fetch(rolesApiUrl)
        .then(res => {
            if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
            return res.json();
        })
        .then(data => {
            if (!Array.isArray(data)) throw new Error('Roles data is not an array');
            getRoles(data);
        })
        .catch(error => console.log('Ошибка при загрузке ролей:', error));
};

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
        `;
        rolesArr.push(role);
    });
    newRoles.innerHTML = rolesOptions;
    editRoles.innerHTML = rolesOptions;
    delRoles.innerHTML = rolesOptions;
};

const refreshListOfUsers = () => {
    fetch(usersApiUrl)
        .then(res => res.json())
        .then(data => {
            result = '';
            getAllUsers(data);
        })
};

const on = (element, event, selector, handler) => {
    element.addEventListener(event, e => {
        if (e.target.closest(selector)) {
            handler(e);
        }
    });
};

const cities = ["Moscow", "Saint Petersburg"];
on(document, 'click', '.btnDelete', e => {
    const row = e.target.parentNode.parentNode;
    const idForm = row.children[0].innerHTML;
    const firstNameForm = row.children[1].innerHTML;
    const lastNameForm = row.children[2].innerHTML;
    const usernameForm = row.children[3].innerHTML;
    const sexForm = row.children[5].innerHTML.trim();
    const cityForm = row.children[6].innerHTML.trim();

    delId.value = idForm;
    delName.value = firstNameForm;
    delSurname.value = lastNameForm;
    delUsername.value = usernameForm;

    document.querySelector('#maleDelete').checked = (sexForm === 'male');
    document.querySelector('#femaleDelete').checked = (sexForm === 'female');

    cities.forEach(city => {
        const option = document.createElement('option');
        option.value = city;
        option.textContent = city;
        if (city === cityForm) {
            option.selected = true;
        }
        delCity.appendChild(option);
    });

    let rolesOptions = '';
    let userRoless = usersArr.find(user => user.id == idForm)?.roles || [];
    userRoless.forEach(role => {
        rolesOptions += `
            <option value="${role.id}">${role.role}</option>
        `;
        userRolesArr.push(role);
    });

    delRoles.innerHTML = rolesOptions;
    deleteUserModal.show();
});

let idForm = 0;
on(document, 'click', '.btnEdit', e => {
    const row = e.target.parentNode.parentNode;
    idForm = row.children[0].innerHTML;
    const firstNameForm = row.children[1].innerHTML;
    const lastNameForm = row.children[2].innerHTML;
    const usernameForm = row.children[3].innerHTML;
    const sexForm = row.children[5].innerHTML.trim();
    const cityForm = row.children[6].innerHTML.trim();

    editId.value = idForm;
    editName.value = firstNameForm;
    editSurname.value = lastNameForm;
    editUsername.value = usernameForm;

    document.querySelector('#maleUpdate').checked = (sexForm === 'male');
    document.querySelector('#femaleUpdate').checked = (sexForm === 'female');

    const citySelect = document.getElementById('editCity');
    citySelect.innerHTML = ''; // Очищаем список городов
    cities.forEach(city => {
        const option = document.createElement('option');
        option.value = city;
        option.textContent = city;
        if (city === cityForm) {
            option.selected = true;
        }
        citySelect.appendChild(option);
    });

    editRoles.options.selectedIndex = -1;
    editUserModal.show();
});

btnCreate.addEventListener('click', () => {
    newName.value = '';
    newSurname.value = '';
    newUsername.value = '';
    newPassword.value = '';
    const citySelect = document.getElementById('newCity');
    citySelect.innerHTML = '';
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
    fetch(usersApiUrl + delId.value, {
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

    const newUser = {
        name: newName.value,
        surname: newSurname.value,
        username: newUsername.value,
        password: newPassword.value,
        sex: sexForm,
        city: cityForm,
        roles: rolesJ
    };

    console.log(newUser);

    fetch(usersApiUrl, {
        method: 'POST',
        headers: {
            'Content-type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify(newUser)
    })
        .then(res => res.json())
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

    console.log(refreshListOfUsers);
});

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

    const updatedUser = {
        id: editId.value,
        name: editName.value,
        surname: editSurname.value,
        username: editUsername.value,
        password: editPassword.value,
        sex: sexForm,
        city: cityForm,
        roles: rolesJ
    };

    fetch(usersApiUrl + editId.value, {
        method: 'PUT',
        headers: {
            'Content-type': 'application/json; charset=UTF-8'
        },
        body: JSON.stringify(updatedUser)
    })
        .then(res => res.json())
        .catch(err => console.log(err))
        .then(refreshListOfUsers);

    editUserModal.hide();
});

initializeConfig();

document.addEventListener('DOMContentLoaded', function () {
    function fetchUserInfo() {
        fetch('/admin/api/users/current')  // Поменяйте на ваш реальный endpoint
            .then(response => response.json())
            .then(data => {
                if (data) {
                    populateUserInfo(data);
                    updateNavInfo(data);
                }
            })
            .catch(error => console.error('Error fetching user data:', error));
    }

    function populateUserInfo(user) {
        const tableBody = document.querySelector('.user_info_table_body');

        const row = document.createElement('tr');
        let userRoles = user.roles.map(role => role.role).join(' ');
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.name}</td>
            <td>${user.surname}</td>
            <td>${user.username}</td>
            <td>${userRoles}</td>
            <td>${user.sex}</td>
            <td>${user.city}</td>
        `;

        tableBody.appendChild(row);
    }

    function updateNavInfo(user) {
        const usernameNav = document.getElementById('usernameNav');
        const rolesNav = document.getElementById('rolesNav');

        usernameNav.textContent = user.username;
        rolesNav.textContent = user.roles.map(role => role.role).join(', ');
    }

    fetchUserInfo();
});

