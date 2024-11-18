const express = require('express');
const router = express.Router();
const cartController = require('../controllers/cart');

router.get('/getSpec/:cartId', cartController.getSpecCart);
router.get('/:userName', cartController.getUserCarts);
router.post('/', cartController.createCart);

module.exports = router;
