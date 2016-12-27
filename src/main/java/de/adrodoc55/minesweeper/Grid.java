package de.adrodoc55.minesweeper;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Iterables;

public class Grid<E> extends AbstractCollection<E> {
  @FunctionalInterface
  public interface ElementSupplier<E> {
    E supply(Coordinate2D coordinate);
  }

  private final ImmutableList<ImmutableList<E>> matrix;
  private final int size;

  public Grid(int height, int width, ElementSupplier<E> elementSupplier) {
    Builder<ImmutableList<E>> matrix = ImmutableList.builder();
    for (int y = 0; y < height; y++) {
      Builder<E> row = ImmutableList.builder();
      for (int x = 0; x < width; x++) {
        Coordinate2D coordinate = new Coordinate2D(x, y);
        E element = elementSupplier.supply(coordinate);
        checkNotNull(element, "element == null!");
        row.add(element);
      }
      matrix.add(row.build());
    }
    this.matrix = matrix.build();
    size = height * width;
  }

  public @Nullable E getElement(Coordinate2D coordinate) {
    checkNotNull(coordinate, "coordinate == null!");
    ImmutableList<E> row = getNullable(matrix, coordinate.getY());
    if (row == null)
      return null;
    return getNullable(row, coordinate.getX());
  }

  private static @Nullable <E> E getNullable(List<E> list, int i) {
    if (i < 0 || list.size() <= i) {
      return null;
    }
    return list.get(i);
  }

  public Collection<E> getElements(Collection<Coordinate2D> coordinates) {
    Collection<E> result = new ArrayList<>();
    for (Coordinate2D coordinate : coordinates) {
      E element = getElement(coordinate);
      if (element != null)
        result.add(element);
    }
    return result;
  }

  public Collection<E> getNeighbours(Coordinate2D coordinate) {
    return getElements(coordinate.getNeighbours());
  }

  @Override
  public Iterator<E> iterator() {
    return Iterables.concat(matrix).iterator();
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (ImmutableList<E> row : matrix) {
      for (E element : row) {
        sb.append(element);
      }
      sb.append('\n');
    }
    return sb.toString();
  }
}
