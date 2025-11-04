"use client"

import { useState } from "react"
import Sidebar from "./components/sidebar/sidebar"
import ProductList from "./components/products/product-list"
import ProductForm from "./components/products/product-form"
import ProductDetail from "./components/products/product-detail"
import "./assets/css/page.css"
import type { Product } from "./models/Product"

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [currentView, setCurrentView] = useState<"list" | "add" | "edit">("list")
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [detailProduct, setDetailProduct] = useState<Product | null>(null)
  const [products, setProducts] = useState<Product[]>([
    {
      id: 1,
      name: "The Great Gatsby",
      author: "F. Scott Fitzgerald",
      price: 12.99,
      publicationYear: 1925,
      imageUrl: "/great-gatsby-book-cover.png",
      description: "A classic American novel set in the Jazz Age",
      quantity: 15,
    },
    {
      id: 2,
      name: "To Kill a Mockingbird",
      author: "Harper Lee",
      price: 14.99,
      publicationYear: 1960,
      imageUrl: "/to-kill-a-mockingbird-cover.png",
      description: "A gripping tale of racial injustice and childhood innocence",
      quantity: 20,
    },
  ])

  const handleAddProduct = (product: Product) => {
    if (selectedProduct) {
      setProducts(products.map((p) => (p.id === selectedProduct.id ? { ...product, id: selectedProduct.id } : p)))
      setSelectedProduct(null)
    } else {
      setProducts([...products, { ...product, id: Date.now() }])
    }
    setCurrentView("list")
  }

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product)
    setCurrentView("edit")
  }

  const handleDeleteProduct = (id: number) => {
    setProducts(products.filter((p) => p.id !== id))
  }

  const handleLogout = () => {
    setIsLoggedIn(false)
    setCurrentView("list")
  }

  const handleViewDetail = (product: Product) => {
    setDetailProduct(product)
  }

  const handleCloseDetail = () => {
    setDetailProduct(null)
  }

  return (
    <div className="app-container">
      <Sidebar
        isLoggedIn={isLoggedIn}
        onLogin={() => setIsLoggedIn(true)}
        onLogout={handleLogout}
        onViewChange={setCurrentView}
        currentView={currentView}
      />
      <main className="main-content">
        {!isLoggedIn ? (
          <div className="login-prompt">
            <h1>Quản Lý Sách</h1>
            <p>Vui lòng đăng nhập để tiếp tục</p>
            <button className="login-btn" onClick={() => setIsLoggedIn(true)}>
              Đăng Nhập
            </button>
          </div>
        ) : (
          <>
            {currentView === "list" && (
              <ProductList
                products={products}
                onEdit={handleEditProduct}
                onDelete={handleDeleteProduct}
                onAdd={() => setCurrentView("add")}
                onViewDetail={handleViewDetail}
              />
            )}
            {(currentView === "add" || currentView === "edit") && (
              <ProductForm
                product={selectedProduct}
                onSubmit={handleAddProduct}
                onCancel={() => {
                  setCurrentView("list")
                  setSelectedProduct(null)
                }}
              />
            )}
            {detailProduct && <ProductDetail product={detailProduct} onClose={handleCloseDetail} />}
          </>
        )}
      </main>
    </div>
  )
}
