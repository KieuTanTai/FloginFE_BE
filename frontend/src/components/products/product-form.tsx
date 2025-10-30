"use client"

import type React from "react"
import { useState, useEffect } from "react"
import { Save, Plus, X } from "lucide-react"
import "../../assets/css/products/product-form.css"

interface Product {
  id?: number
  title: string
  author: string
  price: number
  year: number
  image?: string
}

interface ProductFormProps {
  product?: Product | null
  onSubmit: (product: Product) => void
  onCancel: () => void
}

export default function ProductForm({ product, onSubmit, onCancel }: ProductFormProps) {
  const [formData, setFormData] = useState<Product>({
    title: "",
    author: "",
    price: 0,
    year: new Date().getFullYear(),
    image: "",
  })
  const [imagePreview, setImagePreview] = useState<string>("")

  useEffect(() => {
    if (product) {
      setFormData(product)
      setImagePreview(product.image || "")
    }
  }, [product])

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: name === "price" ? Number.parseFloat(value) : name === "year" ? Number.parseInt(value) : value,
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
          image: result,
        }))
      }
      reader.readAsDataURL(file)
    }
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    onSubmit(formData)
  }

  return (
    <div className="form-container">
      <div className="form-card">
        <h1>{product ? "Chỉnh Sửa Sách" : "Thêm Sách Mới"}</h1>
        <form onSubmit={handleSubmit}>
          <div className="form-group image-group">
            <label htmlFor="image">Hình Ảnh Sách</label>
            <div className="image-upload-wrapper">
              <input
                type="file"
                id="image"
                name="image"
                onChange={handleImageChange}
                accept="image/*"
                className="image-input"
              />
              {imagePreview && (
                <div className="image-preview">
                  <img src={imagePreview || "/placeholder.svg"} alt="Preview" />
                </div>
              )}
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="title">Tên Sách *</label>
            <input
              type="text"
              id="title"
              name="title"
              value={formData.title}
              onChange={handleChange}
              required
              placeholder="Nhập tên sách"
            />
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

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="year">Năm Xuất Bản *</label>
              <input
                type="number"
                id="year"
                name="year"
                value={formData.year}
                onChange={handleChange}
                required
                min="1000"
                max={new Date().getFullYear()}
              />
            </div>

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
                step="0.01"
              />
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
