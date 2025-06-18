#!/bin/bash

# Professional License Configuration Script for FTEmulator
# Automated AGPL-3.0 license setup and copyright headers
AUTHOR_NAME="Álex Frías"
DEVELOPER_HANDLE="alexwebdev05"
AUTHOR_EMAIL="alexwebdev05@proton.me"
PROJECT_NAME="FTEmulator"
PROJECT_DESCRIPTION="FTEmulator is a high-performance stock market investment simulator designed with extreme technical efficiency"
GITHUB_PROFILE="https://github.com/alexwebdev05"
LOCATION="Spain"
YEAR_START="2025"
YEAR=$(date +%Y)

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 Setting up AGPL-3.0 licensing for $PROJECT_NAME...${NC}"
echo -e "${YELLOW}📋 Project: $PROJECT_DESCRIPTION${NC}"
echo -e "${YELLOW}👨‍💻 Author: $AUTHOR_NAME ($DEVELOPER_HANDLE)${NC}"

# Función para crear archivo LICENSE
create_license() {
    cat > LICENSE << 'EOF'
GNU AFFERO GENERAL PUBLIC LICENSE
Version 3, 19 November 2007

Copyright (C) [YEAR_START] [AUTHOR_NAME]
All rights reserved.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <https://www.gnu.org/licenses/>.

===============================================================================
COMMERCIAL LICENSING
===============================================================================

For commercial use, integration into proprietary software, or any use that
does not comply with the AGPL-3.0 license terms, a separate commercial 
license is required.

Contact Information:
- Email: [AUTHOR_EMAIL]
- GitHub: [GITHUB_PROFILE]
- Developer: [AUTHOR_NAME] ([DEVELOPER_HANDLE])

===============================================================================
PROJECT INFORMATION
===============================================================================

Project: [PROJECT_NAME]
Description: [PROJECT_DESCRIPTION]
Author: [AUTHOR_NAME]
Location: [LOCATION]
EOF
    
    # Replace placeholders with actual values
    sed -i "s/\[AUTHOR_NAME\]/$AUTHOR_NAME/g" LICENSE
    sed -i "s/\[DEVELOPER_HANDLE\]/$DEVELOPER_HANDLE/g" LICENSE
    sed -i "s/\[AUTHOR_EMAIL\]/$AUTHOR_EMAIL/g" LICENSE
    sed -i "s/\[PROJECT_NAME\]/$PROJECT_NAME/g" LICENSE
    sed -i "s/\[PROJECT_DESCRIPTION\]/$PROJECT_DESCRIPTION/g" LICENSE
    sed -i "s/\[GITHUB_PROFILE\]/$GITHUB_PROFILE/g" LICENSE
    sed -i "s/\[LOCATION\]/$LOCATION/g" LICENSE
    sed -i "s/\[YEAR_START\]/$YEAR_START/g" LICENSE
}

# Función para crear template de header
create_header_template() {
    mkdir -p .license-templates
    
    # Professional template for Java/JavaScript/TypeScript
    cat > .license-templates/header.txt << EOF
/*
 * $PROJECT_NAME - $PROJECT_DESCRIPTION
 * 
 * Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
 * Licensed under GNU Affero General Public License v3.0
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * For commercial licensing inquiries, please contact: $AUTHOR_EMAIL
 * GitHub: $GITHUB_PROFILE
 */
EOF

    # Professional template for Python
    cat > .license-templates/header-python.txt << EOF
#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
$PROJECT_NAME - $PROJECT_DESCRIPTION

Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
Licensed under GNU Affero General Public License v3.0

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU Affero General Public License for more details.

For commercial licensing inquiries, please contact: $AUTHOR_EMAIL
GitHub: $GITHUB_PROFILE
"""
EOF

    # Professional template for YAML/Dockerfile
    cat > .license-templates/header-yaml.txt << EOF
# $PROJECT_NAME - $PROJECT_DESCRIPTION
# Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
# Licensed under AGPL-3.0 - see LICENSE file
# Commercial licensing: $AUTHOR_EMAIL | GitHub: $GITHUB_PROFILE
EOF
}

# Función para añadir headers a archivos existentes
add_headers_to_existing() {
    echo -e "${YELLOW}📝 Adding professional copyright headers to existing files...${NC}"
    
    # Java files
    find . -name "*.java" -not -path "./.*" -not -path "./target/*" -not -path "./build/*" | while read file; do
        if ! grep -q "Copyright (C)" "$file"; then
            echo -e "${GREEN}  ✓ Adding header to: $file${NC}"
            cat .license-templates/header.txt "$file" > temp && mv temp "$file"
        fi
    done
    
    # JavaScript/TypeScript files
    find . -name "*.js" -o -name "*.ts" -o -name "*.jsx" -o -name "*.tsx" | grep -v node_modules | grep -v dist | grep -v build | while read file; do
        if ! grep -q "Copyright (C)" "$file"; then
            echo -e "${GREEN}  ✓ Adding header to: $file${NC}"
            cat .license-templates/header.txt "$file" > temp && mv temp "$file"
        fi
    done
    
    # Python files
    find . -name "*.py" -not -path "./.*" -not -path "./__pycache__/*" -not -path "./venv/*" | while read file; do
        if ! grep -q "Copyright (C)" "$file"; then
            echo -e "${GREEN}  ✓ Adding header to: $file${NC}"
            # For Python, preserve existing shebang if present
            if head -n1 "$file" | grep -q "^#!"; then
                # Keep shebang, add header after
                head -n1 "$file" > temp
                echo "" >> temp
                tail -n+2 .license-templates/header-python.txt >> temp
                echo "" >> temp
                tail -n+2 "$file" >> temp
                mv temp "$file"
            else
                cat .license-templates/header-python.txt "$file" > temp && mv temp "$file"
            fi
        fi
    done
    
    # YAML/Docker files
    find . -name "*.yml" -o -name "*.yaml" -o -name "Dockerfile*" -o -name "*.tf" | grep -v node_modules | while read file; do
        if ! grep -q "Copyright (C)" "$file"; then
            echo -e "${GREEN}  ✓ Adding header to: $file${NC}"
            cat .license-templates/header-yaml.txt "$file" > temp && mv temp "$file"
        fi
    done
}

# Función para actualizar README
update_readme() {
    if [ -f README.md ]; then
        if ! grep -q "## 📄 License" README.md; then
            echo -e "${YELLOW}📄 Updating README.md with professional licensing information...${NC}"
            cat >> README.md << EOF

## 📄 License

**$PROJECT_NAME** is licensed under the **GNU Affero General Public License v3.0 (AGPL-3.0)**.

### 📋 Project Information
- **Project:** $PROJECT_NAME
- **Description:** $PROJECT_DESCRIPTION
- **Author:** $AUTHOR_NAME ([$DEVELOPER_HANDLE]($GITHUB_PROFILE))
- **License:** AGPL-3.0
- **Copyright:** © $YEAR_START-$YEAR $AUTHOR_NAME

### 💼 Commercial Licensing

For commercial use, proprietary integration, or any application that doesn't comply with AGPL-3.0 terms, a separate commercial license is required.

**Commercial Licensing Contact:**
- 📧 Email: $AUTHOR_EMAIL  
- 🐙 GitHub: [$GITHUB_PROFILE]($GITHUB_PROFILE)
- 👨‍💻 Developer: $AUTHOR_NAME ($DEVELOPER_HANDLE)

### ⚖️ License Terms

| Usage Type | AGPL-3.0 | Commercial License |
|------------|:--------:|:------------------:|
| Open source projects | ✅ Free | ✅ Available |
| Educational/Research | ✅ Free | ✅ Available |
| Internal business use | ✅ Free* | ✅ Recommended |
| Commercial products | ❌ Restricted | ✅ Required |
| Proprietary software | ❌ Not allowed | ✅ Required |
| SaaS/Web services | ⚠️ Must open source | ✅ Required |

*Internal use must comply with AGPL-3.0 copyleft requirements.

### 🔒 Why AGPL-3.0?

The AGPL-3.0 license ensures that:
- The software remains free and open for educational and research purposes
- Any modifications or improvements are shared back with the community
- Commercial entities contribute fairly through licensing fees
- The project maintains sustainable development funding

### 📞 Contact

For licensing inquiries, technical questions, or collaboration opportunities:

- **Email:** $AUTHOR_EMAIL
- **GitHub:** [$GITHUB_PROFILE]($GITHUB_PROFILE)
- **Location:** $LOCATION

EOF
        fi
    fi
}

# Función para crear hooks de Git
create_git_hooks() {
    echo -e "${YELLOW}🔧 Setting up automated Git hooks for license headers...${NC}"
    
    mkdir -p .git/hooks
    
    # Professional pre-commit hook for automatic header insertion
    cat > .git/hooks/pre-commit << 'HOOK_EOF'
#!/bin/bash

# FTEmulator - Automated License Header Pre-commit Hook
# Ensures all new files have appropriate copyright headers

AUTHOR_NAME="REPLACE_AUTHOR_NAME"
DEVELOPER_HANDLE="REPLACE_DEVELOPER_HANDLE"
AUTHOR_EMAIL="REPLACE_AUTHOR_EMAIL"
PROJECT_NAME="REPLACE_PROJECT_NAME"
PROJECT_DESCRIPTION="REPLACE_PROJECT_DESCRIPTION"
GITHUB_PROFILE="REPLACE_GITHUB_PROFILE"
YEAR_START="REPLACE_YEAR_START"
YEAR=$(date +%Y)

# Function to add professional header if missing
add_header_if_missing() {
    local file=$1
    local header_type=$2
    
    if [ ! -f "$file" ] || grep -q "Copyright (C)" "$file"; then
        return
    fi
    
    case $header_type in
        "java"|"js"|"ts")
            header="/*
 * $PROJECT_NAME - $PROJECT_DESCRIPTION
 * 
 * Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
 * Licensed under GNU Affero General Public License v3.0
 * 
 * For commercial licensing inquiries, please contact: $AUTHOR_EMAIL
 * GitHub: $GITHUB_PROFILE
 */

"
            ;;
        "python")
            header="#!/usr/bin/env python3
# -*- coding: utf-8 -*-

\"\"\"
$PROJECT_NAME - $PROJECT_DESCRIPTION

Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
Licensed under GNU Affero General Public License v3.0

For commercial licensing inquiries, please contact: $AUTHOR_EMAIL
GitHub: $GITHUB_PROFILE
\"\"\"

"
            ;;
        "yaml")
            header="# $PROJECT_NAME - $PROJECT_DESCRIPTION
# Copyright (C) $YEAR_START-$YEAR $AUTHOR_NAME ($DEVELOPER_HANDLE)
# Licensed under AGPL-3.0 - Commercial licensing: $AUTHOR_EMAIL

"
            ;;
    esac
    
    echo "$header$(cat "$file")" > "$file"
    git add "$file"
    echo "✓ Added professional header to: $file"
}

# Process staged files for new additions
git diff --cached --name-only --diff-filter=A | while read file; do
    case "$file" in
        *.java) add_header_if_missing "$file" "java" ;;
        *.js|*.ts|*.jsx|*.tsx) add_header_if_missing "$file" "js" ;;
        *.py) add_header_if_missing "$file" "python" ;;
        *.yml|*.yaml|Dockerfile*|*.tf) add_header_if_missing "$file" "yaml" ;;
    esac
done
HOOK_EOF

    # Replace placeholders in the hook with actual values
    sed -i "s/REPLACE_AUTHOR_NAME/$AUTHOR_NAME/g" .git/hooks/pre-commit
    sed -i "s/REPLACE_DEVELOPER_HANDLE/$DEVELOPER_HANDLE/g" .git/hooks/pre-commit
    sed -i "s/REPLACE_AUTHOR_EMAIL/$AUTHOR_EMAIL/g" .git/hooks/pre-commit
    sed -i "s/REPLACE_PROJECT_NAME/$PROJECT_NAME/g" .git/hooks/pre-commit
    sed -i "s|REPLACE_PROJECT_DESCRIPTION|$PROJECT_DESCRIPTION|g" .git/hooks/pre-commit
    sed -i "s|REPLACE_GITHUB_PROFILE|$GITHUB_PROFILE|g" .git/hooks/pre-commit
    sed -i "s/REPLACE_YEAR_START/$YEAR_START/g" .git/hooks/pre-commit
    
    chmod +x .git/hooks/pre-commit
}

# Función principal
main() {
    echo -e "${GREEN}🏗️  Configuring repository: $(basename $(pwd))${NC}"
    echo -e "${YELLOW}📊 Project: $PROJECT_NAME${NC}"
    echo -e "${YELLOW}👨‍💻 Developer: $AUTHOR_NAME ($DEVELOPER_HANDLE)${NC}"
    echo -e "${YELLOW}📧 Contact: $AUTHOR_EMAIL${NC}"
    
    # Create professional LICENSE file
    create_license
    echo -e "${GREEN}  ✓ Professional LICENSE file created${NC}"
    
    # Create header templates
    create_header_template
    echo -e "${GREEN}  ✓ Professional header templates created${NC}"
    
    # Add headers to existing files
    add_headers_to_existing
    
    # Update README with comprehensive licensing info
    update_readme
    echo -e "${GREEN}  ✓ README.md updated with professional licensing section${NC}"
    
    # Configure automated Git hooks
    if [ -d .git ]; then
        create_git_hooks
        echo -e "${GREEN}  ✓ Automated Git hooks configured${NC}"
    fi
    
    echo -e "${GREEN}✨ Professional licensing configuration completed!${NC}"
    echo -e "${YELLOW}💡 All future files will automatically receive professional copyright headers${NC}"
}

# Ejecutar función principal
main

# Limpiar templates temporales
rm -rf .license-templates

echo -e "${GREEN}🎉 FTEmulator licensing setup completed successfully!${NC}"
echo -e "${GREEN}🔒 Your repository is now professionally protected with AGPL-3.0${NC}"
echo -e ""
echo -e "${YELLOW}📋 Next Steps:${NC}"
echo -e "   1. Review changes: ${GREEN}git status${NC}"
echo -e "   2. Commit changes: ${GREEN}git add . && git commit -m 'feat: Add professional AGPL-3.0 licensing and copyright headers'${NC}"
echo -e "   3. Push to GitHub: ${GREEN}git push${NC}"
echo -e ""
echo -e "${YELLOW}💼 Commercial Licensing:${NC}"
echo -e "   📧 Contact: $AUTHOR_EMAIL"
echo -e "   🐙 GitHub: $GITHUB_PROFILE"
echo -e "   👨‍💻 Developer: $AUTHOR_NAME ($DEVELOPER_HANDLE)"
echo -e ""
echo -e "${GREEN}✨ All future files will automatically include professional copyright headers!${NC}"