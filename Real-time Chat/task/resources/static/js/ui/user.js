import {state} from '../state.js'
import {openPrivateChat} from './messages.js'

// Юзеры в чате
// user: { unread: number, lastMessageTime: number }
const usersContainer = document.getElementById("users");

export function renderUsers(users) {
    usersContainer.innerHTML = "";

    [...users.keys()]
        .filter(user => user !== state.username) // исключаем себя
        .sort((a, b) => {
                    const ta = state.usersState.get(a)?.lastMessageTime || 0;
                    const tb = state.usersState.get(b)?.lastMessageTime || 0;
                    return tb - ta; // новые сверху
                })
        .forEach(user => {
            addUserToDOM(user);
        });
}

export function updateUsers(users){
    state.usersState = new Map();
    users.forEach(user => {
          state.usersState.set(user, {
                    unread: 0,
                    lastMessageTime: 0
                });
      });

}

function addUserToDOM(user) {
    const container = document.createElement("div");
    container.classList.add("user-container");

    const userDiv = document.createElement("div");
    userDiv.classList.add("user");
    userDiv.textContent = user;

    const counter = document.createElement("div");
    counter.classList.add("new-message-counter");

    const unread = state.usersState.get(user)?.unread || 0;

    if (unread > 0) {
        counter.textContent = unread;
        counter.style.display = "block";
    } else {
        counter.style.display = "none";
    }

    container.appendChild(userDiv);
    container.appendChild(counter);

    container.addEventListener("click", () => {
        openPrivateChat(user);
    });

    usersContainer.appendChild(container);
    console.log(user)
}