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
  quantity?: number;
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
  quantity: 10,
});
