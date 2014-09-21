echo "Converting Dia generated tex images to proper tex code..."
files="img/*.tex"
sed -i ".bak" 's/\\\$/\$/g' $files
sed -i ".bak" 's/\\_/_/g' $files
sed -i ".bak" 's/\\{/{/g' $files
sed -i ".bak" 's/\\}/}/g' $files
sed -i ".bak" 's/\\ensuremath{\\backslash}/\\/g' $files
