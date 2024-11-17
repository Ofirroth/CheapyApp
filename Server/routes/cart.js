const express = require('express');
const router = express.Router();
const cartController = require('../controllers/cart');

router.post('/', cartController.createCart);

// Get a user's cart by ID
router.get('/:cartId', cartController.getCartById);

// Update a cart (e.g., add or remove items)
router.put('/:cartId', cartController.updateCart);

// Delete a cart
router.delete('/:cartId', cartController.deleteCart);

module.exports = router;
