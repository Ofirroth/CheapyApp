const messageController = require('../controllers/message');

const express = require('express');
var router = express.Router();

router.route('/:id/Messages')
    .get(messageController.getMessages)
    .post(messageController.createMessage);

module.exports = router;