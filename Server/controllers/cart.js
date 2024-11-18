const cartService = require('../services/cart');

const createCart = async (req, res) => {
  try {
    const cartData = req.body;
    const newCart = await cartService.createCart(cartData);
    res.status(200).json(newCart);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getUserCarts = async (req, res) => {
  try {
    const { userName } = req.params;
    const carts = await cartService.getUserCarts(userName);
    if (carts) res.status(200).json(carts);
    else res.status(404).json({ message: 'Cart not found' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

const getSpecCart = async (req, res) => {
  try {
    const { cartId } = req.params;

    const cart = await cartService.getSpecCart(cartId);
    if (cart) {
      res.status(200).json(cart);
    } else {
      res.status(404).json({ message: 'Cart not found' });
    }
  } catch (error) {
    console.log('9999');
    res.status(500).json({ error: error.message });
  }
};

module.exports = {
  createCart,
  getUserCarts,
  getSpecCart,
};
