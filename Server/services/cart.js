const Cart = require('../models/cart'); // Assuming Cart model is set up as discussed
const Item = require('../models/item');
const User = require('../models/user');
const Store = require('../models/store'); // Assuming Store model exists

// Create a new cart
exports.createCart = async (cartData) => {
  try {
    const { storeId, totalPrice, dateCreated, items, userId } = cartData;
    const cart = new Cart({ storeId, totalPrice, dateCreated, items, userId });
    return await cart.save();
  } catch (error) {
  console.log(error.message);
    throw new Error(`Error creating cart: ${error.message}`);
  }
};

exports.getUserCarts = async (userName) => {
  try {
    const user = await User.findOne({ username: userName });
    if (!user) throw new Error(`User with username ${userName} not found`);

    const carts = await Cart.find({ userId: user._id });
    if (!carts || carts.length === 0) throw new Error('No carts found');

    const cartDetails = await Promise.all(
      carts.map(async (cart) => {
        const store = await Store.findById(cart.storeId); // Fetch the store by its ID
        if (!store) throw new Error(`Store with ID ${cart.storeId} not found`);
        return {
          cartId: cart._id,
          storeId: cart.storeId,                 // Matches getStoreId
          userId: cart.userId,                   // Matches getUserId
          storeName: store.name,                 // Matches getStoreName
          date: cart.dateCreated.toISOString(),  // Matches getDate (formatted as string)
          storeCity: store.city,                 // Matches getStoreCity
          storeImage: store.image,               // Matches getStoreImage
          totalPrice: cart.totalPrice.toFixed(2) // Matches getTotalPrice (formatted as string)
        };
      })
    );
    return cartDetails;  // Return the formatted cart details

  } catch (error) {
    throw new Error(`Error fetching user carts: ${error.message}`);
  }
};

exports.getSpecCart = async (cartId) => {
  try {
    const cart = await Cart.findOne({'_id': cartId});
    return cart.items;
  } catch (error) {
    throw new Error('Error fetching cart by ID: ' + error.message);
  }
};
