"use client"
import "../../assets/css/products/product-detail.css"
import type { Product } from "../../models/Product"

interface ProductDetailProps {
  product: Product
  onClose: () => void
}

export default function ProductDetail({ product, onClose }: ProductDetailProps) {
  return (
    <div className="detail-overlay" onClick={onClose}>
      <div className="detail-modal" onClick={(e) => e.stopPropagation()}>
        <div className="detail-image-section">
          {product.imageUrl && (
            <img src={product.imageUrl || "/placeholder.svg"} alt={product.name} className="detail-image" />
          )}
          <button className="close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="detail-info-section">
          <h2 className="detail-title">{product.name}</h2>
          
          {product.description && (
            <div className="detail-description">
              <p>{product.description}</p>
            </div>
          )}
          
          <div className="detail-meta">
            <div className="meta-item">
              <span className="meta-label">Tác giả:</span>
              <span className="meta-value">{product.author}</span>
            </div>
            {product.category && (
              <div className="meta-item">
                <span className="meta-label">Thể loại:</span>
                <span className="meta-value">{product.category.name}</span>
              </div>
            )}
            <div className="meta-item">
              <span className="meta-label">Năm xuất bản:</span>
              <span className="meta-value">{product.publicationYear}</span>
            </div>
            <div className="meta-item">
              <span className="meta-label">Số lượng:</span>
              <span className="meta-value">{product.quantity || 0}</span>
            </div>
            <div className="meta-item">
              <span className="meta-label">Giá:</span>
              <span className="meta-price">${product.price.toFixed(2)}</span>
            </div>
          </div>
          <div className="detail-actions">
            <button className="action-btn primary-btn" onClick={onClose}>
              Đóng
            </button>
          </div>
        </div>
      </div>
    </div>
  )
}
