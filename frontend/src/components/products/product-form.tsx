"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Save, Plus, X } from "lucide-react"
import "../../assets/css/products/product-form.css"
import type { Product } from "../../models/Product"
import { createEmptyProduct } from "../../models/Product"
import type { Category } from "../../models/Category"
import { categoryService } from "../../services/categoryService"
import { validation } from "../../utils/utils"

interface ProductFormProps {
  product?: Product | null
  onSubmit: (product: Product) => void
  onCancel: () => void
}

export default function ProductForm({ product, onSubmit, onCancel }: ProductFormProps) {
  const [formData, setFormData] = useState<Product>(createEmptyProduct())
  const [imagePreview, setImagePreview] = useState<string>("")
  const [categories, setCategories] = useState<Category[]>([])
  const [errors, setErrors] = useState<Record<string, string>>({})

  useEffect(() => {
    // Load categories
    categoryService.getAllCategories().then(setCategories).catch(console.error)
  }, [])

  useEffect(() => {
    if (product) {
      setFormData(product)
      setImagePreview(product.imageUrl || "")
    }
  }, [product])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: name === "price" ? Number.parseFloat(value) : name === "publicationYear" || name === "quantity" ? Number.parseInt(value) : value,
    }))
  }

  const handleCategoryChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
    const categoryId = Number.parseInt(e.target.value)
    const selectedCategory = categories.find(c => c.id === categoryId)
    setFormData((prev) => ({
      ...prev,
      category: selectedCategory,
    }))
  }

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onloadend = () => {
        const result = reader.result as string
        setImagePreview(result)
        setFormData((prev) => ({
          ...prev,
          imageUrl: result,
        }))
      }
      reader.readAsDataURL(file)
    }
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    
    // Validate all fields
    const newErrors: Record<string, string> = {}
    
    const nameValidation = validation.validateProductName(formData.name)
    if (!nameValidation.valid) {
      newErrors.name = nameValidation.error || ''
    }
    
    const priceValidation = validation.validatePrice(formData.price)
    if (!priceValidation.valid) {
      newErrors.price = priceValidation.error || ''
    }
    
    const quantityValidation = validation.validateQuantity(formData.quantity || 0)
    if (!quantityValidation.valid) {
      newErrors.quantity = quantityValidation.error || ''
    }
    
    const descriptionValidation = validation.validateDescription(formData.description || '')
    if (!descriptionValidation.valid) {
      newErrors.description = descriptionValidation.error || ''
    }
    
    const categoryValidation = validation.validateCategory(formData.category?.id)
    if (!categoryValidation.valid) {
      newErrors.category = categoryValidation.error || ''
    }
    
    // If there are errors, don't submit
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors)
      return
    }
    
    // Clear errors and submit
    setErrors({})
    onSubmit(formData)
  }

  return (
    <div className="form-container">
      <div className="form-card">
        <h1>{product ? "Chỉnh Sửa Sách" : "Thêm Sách Mới"}</h1>
        <form onSubmit={handleSubmit}>
          {/* Image Preview */}
          {imagePreview && (
            <div className="image-preview-section">
              <img src={imagePreview || "/placeholder.svg"} alt="Preview" />
            </div>
          )}

          <div className="form-grid">
            {/* Left Column */}
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="name">Tên Sách *</label>
                <input
                  type="text"
                  id="name"
                  name="name"
                  value={formData.name}
                  onChange={handleChange}
                  required
                  placeholder="Nhập tên sách (3-100 từ)"
                />
                {errors.name && <span className="error-message">{errors.name}</span>}
              </div>

              <div className="form-group">
                <label htmlFor="author">Tác Giả *</label>
                <input
                  type="text"
                  id="author"
                  name="author"
                  value={formData.author}
                  onChange={handleChange}
                  required
                  placeholder="Nhập tên tác giả"
                />
              </div>

              <div className="form-group">
                <label htmlFor="category">Thể Loại *</label>
                <select
                  id="category"
                  name="category"
                  value={formData.category?.id || ""}
                  onChange={handleCategoryChange}
                  required
                >
                  <option value="">-- Chọn thể loại --</option>
                  {categories.map((category) => (
                    <option key={category.id} value={category.id}>
                      {category.name}
                    </option>
                  ))}
                </select>
                {errors.category && <span className="error-message">{errors.category}</span>}
              </div>

              <div className="form-row">
                <div className="form-group">
                  <label htmlFor="price">Giá ($) *</label>
                  <input
                    type="number"
                    id="price"
                    name="price"
                    value={formData.price}
                    onChange={handleChange}
                    required
                    min="0"
                    max="999999999"
                    step="0.01"
                  />
                  {errors.price && <span className="error-message">{errors.price}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="quantity">Số Lượng *</label>
                  <input
                    type="number"
                    id="quantity"
                    name="quantity"
                    value={formData.quantity || 10}
                    onChange={handleChange}
                    required
                    min="0"
                    max="99999"
                  />
                  {errors.quantity && <span className="error-message">{errors.quantity}</span>}
                </div>
              </div>
            </div>

            {/* Right Column */}
            <div className="form-column">
              <div className="form-group">
                <label htmlFor="image">Hình Ảnh Sách</label>
                <input
                  type="file"
                  id="image"
                  name="image"
                  onChange={handleImageChange}
                  accept="image/*"
                  className="image-input"
                />
              </div>

              <div className="form-group">
                <label htmlFor="publicationYear">Năm Xuất Bản *</label>
                <input
                  type="number"
                  id="publicationYear"
                  name="publicationYear"
                  value={formData.publicationYear}
                  onChange={handleChange}
                  required
                  min="1000"
                  max={new Date().getFullYear()}
                />
              </div>

              <div className="form-group">
                <label htmlFor="description">Mô Tả</label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description || ""}
                  onChange={handleChange}
                  placeholder="Nhập mô tả sách (tối đa 500 ký tự)"
                  rows={7}
                />
                {errors.description && <span className="error-message">{errors.description}</span>}
              </div>
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="submit-btn">
              {product ? <><Save size={18} /> Cập Nhật</> : <><Plus size={18} /> Thêm Sách</>}
            </button>
            <button type="button" className="cancel-btn" onClick={onCancel}>
              <X size={18} /> Hủy
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}
