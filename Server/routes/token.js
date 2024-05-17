const tokenContorller = require('../controllers/token');

const express = require('express');
var router = express.Router();

router.route('/').post(tokenContorller.processLogin);

module.exports = router;