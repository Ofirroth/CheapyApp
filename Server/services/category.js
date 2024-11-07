const Category = require('../models/category');

const getAllCategories = async () => {
  try {
    const categories = await Category.find().select('-_id');
    return categories;
  } catch (error) {
    throw new Error('Error fetching categories');
  }
};

const getCategoryById = async (id) => {
  try {
    const category = await Category.findById(id);
    if (!category) {
      throw new Error('Category not found');
    }
    return category;
  } catch (error) {
    throw new Error('Error fetching category');
  }
};


module.exports = {
  getAllCategories,
  getCategoryById,
};
