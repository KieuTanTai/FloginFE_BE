"use client"
import "../../assets/css/products/product-detail.css"

interface Product {
  id: number
  title: string
  author: string
  price: number
  year: number
  image?: string
}

interface ProductDetailProps {
  product: Product
  onClose: () => void
}

export default function ProductDetail({ product, onClose }: ProductDetailProps) {
  return (
    <div className="detail-overlay" onClick={onClose}>
      <div className="detail-modal" onClick={(e) => e.stopPropagation()}>
        <div className="detail-image-section">
          {product.image && (
            <img src={product.image || "/placeholder.svg"} alt={product.title} className="detail-image" />
          )}
          <button className="close-btn" onClick={onClose}>
            ✕
          </button>
        </div>

        <div className="detail-info-section">
          <h2 className="detail-title">{product.title}</h2>
          <div className="detail-meta">
            <div className="meta-item">
              <span className="meta-label">Tác giả:</span>
              <span className="meta-value">{product.author}</span>
            </div>
            <div className="meta-item">
              <span className="meta-label">Năm xuất bản:</span>
              <span className="meta-value">{product.year}</span>
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
