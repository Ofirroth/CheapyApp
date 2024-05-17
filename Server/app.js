
const express = require('express');
var app = express();

const http = require("http");
const {Server} = require("socket.io");

const bodyParser = require ('body-parser');
app.use(bodyParser.urlencoded({extended:true}));
app.use(express.json());

const cors = require('cors');
app.use(cors());

const customEnv = require('custom-env');
customEnv.env(process.env.NODE_ENV, './config');

const mongoose = require('mongoose');
mongoose.connect(process.env.CONNECTION_STRING + "ChatDB", {
    useNewURLParser: true,
    useUnifiedTopology: true
});

const users =require ('./routes/user');
app.use('/api/Users', users);

const token = require('./routes/token');
app.use('/api/Tokens',token);

const chat = require('./routes/chat');
const { Socket } = require('dgram');
app.use('/api/Chats',chat);

app.use('/',express.static('public'));
app.use('/Chat',express.static('public'));
app.use('/SignUp',express.static('public'));

app.use(cors());
const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST", "DELETE"],
    },
});

const socketMap = require('./socketMap');
const tokenMap = require('./tokenMap');

const admin = require('firebase-admin');
const serviceAccount = require('./cheapy-firebase.json');

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
    });

io.on("connection", (socket) => {
    socket.on("join_chat", (data) => {
        socket.join(data);
        socketMap.set(data,socket);
        delete tokenMap.get(data);
    });

    socket.on("add-contact",(username) => {
        if(!socket.in(username)){
            return;
        }
        socket.in(username).emit("add-contact");
    })
    socket.on("send_message", (data) => {
        console.log(data);
        socket.in(data.receiverUser).emit("receive_message",data);
    });
})

server.listen(process.env.PORT);