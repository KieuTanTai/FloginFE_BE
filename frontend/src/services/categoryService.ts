import api from "./api";
import type { Category } from "../models/Category";

/**
 * Service for Category-related API calls
 */
export const categoryService = {
  /**
   * Get all categories
   */
  getAllCategories: async (): Promise<Category[]> => {
    const response = await api.get<Category[]>("/categories");
    return response.data;
  },

  /**
   * Get category by ID
   */
  getCategoryById: async (id: number): Promise<Category> => {
    const response = await api.get<Category>(`/categories/${id}`);
    return response.data;
  },
};
