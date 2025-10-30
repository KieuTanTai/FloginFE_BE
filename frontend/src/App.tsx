"use client"

import { useState } from "react"
import Sidebar from "./components/sidebar/sidebar"
import ProductList from "./components/products/product-list"
import ProductForm from "./components/products/product-form"
import ProductDetail from "./components/products/product-detail"
import "./assets/css/page.css"

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [currentView, setCurrentView] = useState<"list" | "add" | "edit">("list")
  const [selectedProduct, setSelectedProduct] = useState(null)
  const [detailProduct, setDetailProduct] = useState<any>(null)
  const [products, setProducts] = useState([
    {
      id: 1,
      title: "The Great Gatsby",
      author: "F. Scott Fitzgerald",
      price: 12.99,
      year: 1925,
      image: "/great-gatsby-book-cover.png",
    },
    {
      id: 2,
      title: "To Kill a Mockingbird",
      author: "Harper Lee",
      price: 14.99,
      year: 1960,
      image: "/to-kill-a-mockingbird-cover.png",
    },
  ])

  const handleAddProduct = (product: any) => {
    if (selectedProduct) {
      setProducts(products.map((p) => (p.id === selectedProduct.id ? { ...product, id: selectedProduct.id } : p)))
      setSelectedProduct(null)
    } else {
      setProducts([...products, { ...product, id: Date.now() }])
    }
    setCurrentView("list")
  }

  const handleEditProduct = (product: any) => {
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

  const handleViewDetail = (product: any) => {
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
