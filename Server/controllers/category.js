// controllers/categoryController.js
const categoryService = require('../services/category');

// Get all categories
async function getAllCategories(req, res) {
  try {
    const categories = await categoryService.getAllCategories();
    res.status(200).json(categories);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
}

// Get category by ID
async function getCategoryById(req, res) {
  const { id } = req.params;
  try {
    const category = await categoryService.getCategoryById(id);
    if (!category) {
      return res.status(404).json({ message: 'Category not found' });
    }
    res.status(200).json(category);
  } catch (error) {
    res.status(500).json({ message: error.message });
  }
}



module.exports = {
  getAllCategories,
  getCategoryById,
};
