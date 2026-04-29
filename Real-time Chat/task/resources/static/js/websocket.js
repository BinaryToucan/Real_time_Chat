import { renderPublicMessages, showMessage } from './ui/messages.js';
import { renderUsers, updateUsers } from './ui/user.js';
import {state} from './state.js'

export let stompClient = null;

export function connect() {

    const socket = new SockJS(`/ws-chat?username=${state.username}`);
    stompClient = Stomp.over(socket);

    stompClient.connect({}, function () {
        console.log("Connected");

        // добавляем юзера
        stompClient.send("/app/user.add",
            {}, JSON.stringify({
                    username: state.username,
        }));

        // загружаем сообщения
        renderPublicMessages();

        // список пользователей
        stompClient.subscribe('/topic/users', function (message) {
            console.log("users - list")
            const users = JSON.parse(message.body);

            updateUsers(users);
            renderUsers(state.usersState);
        });

        // публичный чат
        stompClient.subscribe("/topic/public", function (message) {

            // загружаем только если юзер открыл публичный чат
            if (state.currentChat ==  "public") {
                showMessage(JSON.parse(message.body));
            }
        });

        // приватные сообщения
        stompClient.subscribe("/user/queue/messages", function (message) {
            console.log("/queue/messages")
            const msg = JSON.parse(message.body);

            showMessage(msg);
        });
    });
}