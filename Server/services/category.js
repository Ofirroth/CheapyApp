const Category = require('../models/category');
const SubCategory = require('../models/subCategory');

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
    const category = await Category.find({ id: id });
    if (!category) {
      throw new Error('Category not found');
    }
    return category;
  } catch (error) {
    throw new Error('Error fetching category');
  }
};

const getSubcategoriesById = async (id) => {
  try {
    const subcategories = await SubCategory.find({ parent: id });
    if (!subcategories) {
      throw new Error('Category not found');
    }
    return subcategories;
  } catch (error) {
    throw new Error('Error fetching subcategories');
  }
};



module.exports = {
  getAllCategories,
  getCategoryById,
  getSubcategoriesById
};
