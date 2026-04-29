import { connect, stompClient } from "./websocket.js";
import { renderUsers} from './ui/user.js';
import { renderPublicMessages, input } from './ui/messages.js';
import { state } from './state.js'

const btn = document.getElementById('send-msg-btn');

btn.addEventListener('click', function() {
    const message = input.value.trim();
    if (message === "") return;

    const msgObj = {
        sender: state.username,
        content: message,
        type: state.currentChat == "public" ? "PUBLIC" : "PRIVATE",
        receiver: state.currentChat == "public" ? null : state.currentChat
    };

    stompClient.send("/app/chat.send", {}, JSON.stringify(msgObj));
    //publicMessages.push(msgObj);

    // обновляем порядок
    if (state.currentChat !== "public") {
        state.usersState.get(state.currentChat).lastMessageTime = Date.now();
        renderUsers(state.usersState);
    }

    input.value = "";
});

document.getElementById("send-username-btn").addEventListener("click", () => setUsername());

document.getElementById("public-chat-btn").addEventListener("click", () => {
    if (state.currentChat == "public") return;

    state.currentChat = "public";

    document.getElementById("chat-with").textContent = "Public chat";

    input.value = "";

    renderPublicMessages();
});

function setUsername() {
    state.username = document.getElementById("input-username").value.trim();

    if (!state.username) return;

    connect();

    // скрываем ввод имени
    document.getElementById("username-page").style.display = "none";

    // показываем чат
    document.getElementById("chat-page").style.display = "flex";
    document.getElementById("chat-page").hidden = false;

    input.value = "";
}