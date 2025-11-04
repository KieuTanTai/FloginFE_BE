"use client"
import { Plus, Edit, Trash2 } from "lucide-react"
import "../../assets/css/products/product-list.css"
import type { Product } from "../../models/Product"

interface ProductListProps {
  products: Product[]
  onEdit: (product: Product) => void
  onDelete: (id: number) => void
  onAdd: () => void
  onViewDetail: (product: Product) => void
}

export default function ProductList({ products, onEdit, onDelete, onAdd, onViewDetail }: ProductListProps) {
  return (
    <div className="product-list-container">
      <div className="list-header">
        <h1>Danh Sách Sách</h1>
        <button className="add-btn" onClick={onAdd}>
          <Plus size={18} /> Thêm Sách Mới
        </button>
      </div>

      {products.length === 0 ? (
        <div className="empty-state">
          <p>Chưa có sách nào. Hãy thêm sách mới!</p>
        </div>
      ) : (
        <div className="products-grid">
          {products.map((product) => (
            <div key={product.id} className="product-card">
              <div className="product-image" onClick={() => onViewDetail(product)}>
                {product.imageUrl && <img src={product.imageUrl || "/placeholder.svg"} alt={product.name} />}
              </div>
              <div className="product-info" onClick={() => onViewDetail(product)}>
                <h3 className="product-title">{product.name}</h3>
                <p className="product-author">Tác giả: {product.author}</p>
                <p className="product-year">Năm: {product.publicationYear}</p>
                <p className="product-quantity">Số lượng: {product.quantity || 0}</p>
                <p className="product-price">${product.price.toFixed(2)}</p>
              </div>
              <div className="product-actions">
                <button className="edit-btn" onClick={() => onEdit(product)}>
                  <Edit size={16} /> Sửa
                </button>
                <button className="delete-btn" onClick={() => product.id && onDelete(product.id)}>
                  <Trash2 size={16} /> Xóa
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
