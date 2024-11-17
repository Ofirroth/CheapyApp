const cartService = require('../services/cart');

// Create a new cart
exports.createCart = async (req, res) => {
  try {
    const cartData = req.body;
    console.log(cartData);
    const newCart = await cartService.createCart(cartData);
    res.status(200).json(newCart);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Get a user's cart by ID
exports.getCartById = async (req, res) => {
  try {
    const cartId = req.params.cartId;
    const cart = await cartService.getCartById(cartId);
    if (cart) res.status(200).json(cart);
    else res.status(404).json({ message: 'Cart not found' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Update a cart (e.g., add or remove items)
exports.updateCart = async (req, res) => {
  try {
    const cartId = req.params.cartId;
    const updatedCartData = req.body;
    const updatedCart = await cartService.updateCart(cartId, updatedCartData);
    if (updatedCart) res.status(200).json(updatedCart);
    else res.status(404).json({ message: 'Cart not found' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Delete a cart
exports.deleteCart = async (req, res) => {
  try {
    const cartId = req.params.cartId;
    const result = await cartService.deleteCart(cartId);
    if (result) res.status(200).json({ message: 'Cart deleted successfully' });
    else res.status(404).json({ message: 'Cart not found' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
