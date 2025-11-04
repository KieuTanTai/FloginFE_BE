import api from './api';
import type { Product } from '../models/Product';

export const productService = {
  /**
   * Get all products (not deleted)
   */
  getAllProducts: async (): Promise<Product[]> => {
    const response = await api.get<Product[]>('/products');
    return response.data;
  },

  /**
   * Get product by ID
   */
  getProductById: async (id: number): Promise<Product> => {
    const response = await api.get<Product>(`/products/${id}`);
    return response.data;
  },

  /**
   * Create new product
   */
  createProduct: async (product: Product): Promise<Product> => {
    try {
      const response = await api.post<Product>('/products', product);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to create product. Please try again.');
    }
  },

  /**
   * Update product
   */
  updateProduct: async (id: number, product: Product): Promise<Product> => {
    try {
      const response = await api.put<Product>(`/products/${id}`, product);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update product. Please try again.');
    }
  },

  /**
   * Soft delete product
   */
  softDeleteProduct: async (id: number): Promise<void> => {
    try {
      await api.delete(`/products/${id}`);
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to delete product. Please try again.');
    }
  },

  /**
   * Restore soft deleted product
   */
  restoreProduct: async (id: number): Promise<Product> => {
    try {
      const response = await api.post<Product>(`/products/${id}/restore`);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to restore product. Please try again.');
    }
  },

  /**
   * Update product quantity
   */
  updateQuantity: async (id: number, quantity: number): Promise<Product> => {
    try {
      const response = await api.patch<Product>(`/products/${id}/quantity?quantity=${quantity}`);
      return response.data;
    } catch (error: any) {
      if (error.response?.data?.message) {
        throw new Error(error.response.data.message);
      }
      throw new Error('Failed to update quantity. Please try again.');
    }
  },
};
