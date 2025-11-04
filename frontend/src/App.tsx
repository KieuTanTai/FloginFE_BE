"use client"

import { useState, useEffect } from "react"
import Sidebar from "./components/sidebar/sidebar"
import ProductList from "./components/products/product-list"
import ProductForm from "./components/products/product-form"
import ProductDetail from "./components/products/product-detail"
import LoginForm from "./components/auth/login-form"
import "./assets/css/page.css"
import type { Product } from "./models/Product"
import type { Account } from "./models/Account"
import { productService } from "./services/productService"

export default function App() {
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [currentUser, setCurrentUser] = useState<Account | null>(null)
  const [currentView, setCurrentView] = useState<"list" | "add" | "edit">("list")
  const [selectedProduct, setSelectedProduct] = useState<Product | null>(null)
  const [detailProduct, setDetailProduct] = useState<Product | null>(null)
  const [products, setProducts] = useState<Product[]>([])
  const [isLoading, setIsLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)

  // Load products when logged in
  useEffect(() => {
    if (isLoggedIn) {
      loadProducts()
    }
  }, [isLoggedIn])

  const loadProducts = async () => {
    setIsLoading(true)
    setError(null)
    try {
      const data = await productService.getAllProducts()
      setProducts(data)
    } catch (err: any) {
      setError(err.message || "Failed to load products")
      console.error("Error loading products:", err)
    } finally {
      setIsLoading(false)
    }
  }

  const handleAddProduct = async (product: Product) => {
    setError(null)
    try {
      if (selectedProduct && selectedProduct.id) {
        // Update existing product
        await productService.updateProduct(selectedProduct.id, product)
        setSelectedProduct(null)
      } else {
        // Create new product
        await productService.createProduct(product)
      }
      setCurrentView("list")
      await loadProducts() // Reload products from backend
    } catch (err: any) {
      setError(err.message || "Failed to save product")
      console.error("Error saving product:", err)
    }
  }

  const handleEditProduct = (product: Product) => {
    setSelectedProduct(product)
    setCurrentView("edit")
  }

  const handleDeleteProduct = async (id: number) => {
    setError(null)
    try {
      await productService.softDeleteProduct(id)
      await loadProducts() // Reload products from backend
    } catch (err: any) {
      setError(err.message || "Failed to delete product")
      console.error("Error deleting product:", err)
    }
  }

  const handleLoginSuccess = (account: Account) => {
    setCurrentUser(account)
    setIsLoggedIn(true)
    setCurrentView("list")
  }

  const handleLogout = () => {
    setIsLoggedIn(false)
    setCurrentUser(null)
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
      {!isLoggedIn ? (
        <LoginForm onLoginSuccess={handleLoginSuccess} />
      ) : (
        <>
          <Sidebar
            isLoggedIn={isLoggedIn}
            onLogin={() => setIsLoggedIn(true)}
            onLogout={handleLogout}
            onViewChange={setCurrentView}
            currentView={currentView}
          />
          <main className="main-content">
            {error && (
              <div className="error-banner">
                <p>{error}</p>
                <button onClick={() => setError(null)}>✕</button>
              </div>
            )}
            
            {isLoading ? (
              <div className="loading-state">
                <div className="spinner"></div>
                <p>Loading products...</p>
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
        </>
      )}
    </div>
  )
}
