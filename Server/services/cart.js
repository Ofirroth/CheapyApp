const Cart = require('../models/cart'); // Assuming Cart model is set up as discussed
const Item = require('../models/item');

// Create a new cart
exports.createCart = async (cartData) => {
  try {
    const { storeId, totalPrice, dateCreated, items, userId } = cartData;
    const cart = new Cart({ storeId, totalPrice, dateCreated, items, userId });
    return await cart.save();
  } catch (error) {
    throw new Error(`Error creating cart: ${error.message}`);
  }
};

// Get a cart by ID with populated item details
exports.getCartById = async (cartId) => {
  try {
    const cart = await Cart.findById(cartId).populate('items.item');
    if (!cart) throw new Error('Cart not found');
    return cart;
  } catch (error) {
    throw new Error(`Error fetching cart: ${error.message}`);
  }
};

// Update a cart
exports.updateCart = async (cartId, updatedCartData) => {
  try {
    const updatedCart = await Cart.findByIdAndUpdate(cartId, updatedCartData, { new: true }).populate('items.item');
    if (!updatedCart) throw new Error('Cart not found for update');
    return updatedCart;
  } catch (error) {
    throw new Error(`Error updating cart: ${error.message}`);
  }
};

// Delete a cart
exports.deleteCart = async (cartId) => {
  try {
    const result = await Cart.findByIdAndDelete(cartId);
    if (!result) throw new Error('Cart not found for deletion');
    return result;
  } catch (error) {
    throw new Error(`Error deleting cart: ${error.message}`);
  }
};
