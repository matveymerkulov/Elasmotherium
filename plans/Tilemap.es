import Drawable;

class Tilemap<AnyInt TileInt> extends Drawable {
  Int rowSize, columnSize, cellWidth, cellHeight, maxTileNum;
	Array<TileInt> tiles;
  Array<Image> images;
	assert(tiles.size == rowSize * columnSize);
	assert(images.size >= maxTileNum);

  create(this.rowSize, this.columnSize, this.images, TileInt tileNumber = 0) {
    tilesQuantity = images.size;
    cellWidth = images[0].width;
    cellHeight = images[0].height;
    tiles = Array(rowSize * columnSize, tileNumber);
  }
	
	Int width -> cellWidth * rowSize;
	Int height -> cellHeight * columnSize;
  
  draw(Int x = 0, Int y = 0) {
    for(row = 0 until columnSize)
			for(column = 0 until rowSize)
				images[tiles[column + row * rowSize]].draw(x + column * cellWidth, y + row * cellHeight);
  }
  
  TileInt getAtIndex(Int column, Int row) -> tiles[column + row * rowSize];
  setAtIndex(Int column, Int row, TileInt tileNumber) tiles[column + row * rowSize] = tileNumber;
  
  Int tileX(Int screenX) -> screenX / cellWidth;
  Int tileY(Int screenY) -> screenY / cellHeight;
}