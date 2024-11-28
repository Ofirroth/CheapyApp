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
const scrapeAndSaveItems = require('./playwright/createItems');
const saveMatrixToCSV = require('./recommendationSystem/finalMatrix');
const pythonShell = require('python-shell');

mongoose.connect(process.env.CONNECTION_STRING + "ChatDB", {
    useNewURLParser: true,
    useUnifiedTopology: true
    });

mongoose.connection.on('connected', async () => {
  console.log('Connected to MongoDB');
  await saveMatrixToCSV();
  scrapeAndSaveItems();
});
mongoose.connection.on('error', (err) => {
  console.error('MongoDB connection error:', err);
});

mongoose.connection.on('disconnected', () => {
   console.log('MongoDB disconnected');
});

const users =require ('./routes/user');
app.use('/api/Users', users);

const token = require('./routes/token');
app.use('/api/Tokens',token);

const item = require('./routes/item');
app.use('/api/Item',item);

const store = require('./routes/store');
app.use('/api/Store',store);

const price = require('./routes/price');
app.use('/api/Price',price);

const category = require('./routes/category');
app.use('/api/Category',category);

const cart = require('./routes/cart');
app.use('/api/Cart',cart);

const barcode = require('./routes/barcode');
app.use('/api/barcodes', barcode);

const reco = require('./routes/reco');
app.use('/api/recommended', reco);

app.use('/',express.static('public'));
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

server.listen(process.env.PORT);