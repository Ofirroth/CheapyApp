const express = require('express');
const router = express.Router();
const itemController = require('../controllers/item');

router.get('/', itemController.getItems);
router.get('/:category', itemController.getItemsByCategory);

module.exports = router;