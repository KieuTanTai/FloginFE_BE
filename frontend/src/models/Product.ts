import type { Category } from "./Category";

/**
 * Product entity matching backend ProductDTO
 * Simplified laptop model without author or publication year
 */
export interface Product {
  id?: number;
  name: string;
  imageUrl?: string;
  price: number;
  description?: string;
  category?: Category;
  quantity?: number;
  deleted?: boolean;
}

/**
 * Create an empty Product with default values
 */
export const createEmptyProduct = (): Product => ({
  name: "",
  imageUrl: "",
  price: 0,
  description: "",
  category: undefined,
  quantity: 10,
});
