const { getDefaultConfig } = require("expo/metro-config");
const path = require("path");

const projectRoot = __dirname;
// monorepo root: adjust if your root isn't ../../
const workspaceRoot = path.resolve(projectRoot, "../..");

const config = getDefaultConfig(projectRoot);

// 👇 Let Metro read files from the monorepo root (including libs/)
config.watchFolders = [workspaceRoot];

// Optional, but keeps node_modules resolution sane in a monorepo
config.resolver.nodeModulesPaths = [
  path.resolve(projectRoot, "node_modules"),
  path.resolve(workspaceRoot, "node_modules"),
];

module.exports = config;
