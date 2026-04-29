import {state} from '../state.js';
import {renderUsers} from './user.js';

const messages = document.getElementById("messages");
export const input = document.getElementById("input-msg");

let publicMessages = [];
let privateMessages = {};

export function renderPublicMessages() {
    ///console.log("public")
    state.currentChat = "public";

    fetch("/api/messages/public")
        .then(res => res.json())
        .then(messages => {
            clearMessages();
            messages.forEach(showMessage);
        });
}

function renderPrivateMessages(userB) {
    console.log("private")
    ///console.log("public")
    state.currentChat = userB;

    fetch(`/api/messages/private/${state.username}/${userB}`)
        .then(res => res.json())
        .then(messages => {
            clearMessages();
            messages.forEach(showMessage);
        });
}

export function showMessage(message) {

    const container = document.createElement("div");
    //container.classList.add("message")
    container.classList.add("message-container");

    const sender = document.createElement("div");
    sender.classList.add("sender");
    sender.textContent = message.sender;

    const date = document.createElement("div");
    date.classList.add("date");
    date.textContent = message.timestamp;

    const content = document.createElement("div");
    content.classList.add("message");
    content.textContent = message.content;

    container.appendChild(sender);
    container.appendChild(date);
    container.appendChild(content);

    messages.appendChild(container)

    container.scrollIntoView({"behavior": "smooth"})
}

function clearMessages() {
    document.getElementById("messages").innerHTML = "";
}

export function openPrivateChat(user) {
    state.currentChat = user;
    document.getElementById("chat-with").textContent = user;

    // сброс unread
    if (state.usersState.get(user)) {
        state.usersState.get(user).unread = 0;
    }

    renderUsers(state.usersState);

    input.value = "";

    renderPrivateMessages(user);
}

function openPublicChat() {
    state.currentChat = "public";

    input.value = "";

    fetch("/api/messages/public")
        .then(res => res.json())
        .then(messages => {
            clearMessages();
            messages.forEach(showMessage);
        });


}