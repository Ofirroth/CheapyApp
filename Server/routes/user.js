const express = require ('express');
var router = express.Router();

const userControllers = require('../controllers/user');

router.route('/')
    .post(userControllers.createUser)
    .get(userControllers.getAllUsers)

router.route('/:username').get(userControllers.getUserByToken);
router.route('/getId/:username').get(userControllers.getUserId);
    
module.exports = router;
