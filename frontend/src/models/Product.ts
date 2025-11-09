import type { Category } from "./Category";

/**
 * Product entity matching backend ProductDTO
 */
export interface Product {
  id?: number;
  name: string;
  imageUrl?: string;
  price: number;
  description?: string;
  author: string;
  publicationYear: number;
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
  author: "",
  publicationYear: new Date().getFullYear(),
  category: undefined,
  quantity: 10,
});
