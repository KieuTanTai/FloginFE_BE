module.exports = {
  preset: "ts-jest",
  testEnvironment: "jsdom",

  transform: {
    "^.+\\.(ts|tsx)$": "ts-jest"
  },

  moduleFileExtensions: ["ts", "tsx", "js", "jsx"],

  globals: {
    "ts-jest": {
      tsconfig: "tsconfig.jest.json"
    }
  },
  moduleNameMapper: {
  "\\.(css|less|scss|sass)$": "identity-obj-proxy"
  },
  transformIgnorePatterns: [
    "node_modules/(?!lucide-react)"
  ],
};
