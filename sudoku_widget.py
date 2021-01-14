from collections import namedtuple
import os

from PIL import Image, ImageDraw, ImageFont


LOC = os.path.dirname(os.path.realpath(__file__))

CELL_ATTRS = ('x_min', 'y_min', 'x_max', 'y_max')


def main():
    numbers = {
        (0, 0): {'num': 2, 'color': 'red'},
        (0, 1): {'num': 3, 'color': 'blue'},
        (0, 2): {'num': 5, 'color': 'blue'},
        (1, 1): {'num': 9, 'color': 'black'},
        (1, 2): {'num': 7, 'color': 'black'},
        (2, 0): {'num': 4, 'color': 'black'},
        (2, 2): {'num': 2, 'color': 'red'}
    }

    grid = Grid(3, 3, base_size=(400, 400), margin=(20, 20))
    grid_image = grid.draw(numbers=numbers, width=2, font_name=os.path.join(LOC, 'font.ttf'), font_size=70)

    grid_image.save(os.path.join(LOC, 'res.png'))


class Grid:

    def __init__(self, rows, cols, base_size=(400, 400), margin=(20, 20)):
        self.rows = rows
        self.cols = cols
        self.base_size = base_size
        self.margin = margin
        self.draw_size = (base_size[0] - 2 * margin[0],
                          base_size[1] - 2 * margin[1])
        self.cell_size = (self.draw_size[0] / self.rows,
                          self.draw_size[1] / self.cols)
        self.cells = []
        self._init_cells()

    def _init_cells(self):
        for i in range(self.rows):
            for j in range(self.cols):
                self.cells.append(
                    Cell(self._fit_margin(i * self.cell_size[0], True),
                         self._fit_margin(j * self.cell_size[1], False),
                         self._fit_margin((i + 1) * self.cell_size[0], True),
                         self._fit_margin((j + 1) * self.cell_size[1], False)
                         )
                )

    def _fit_margin(self, pos, row):
        if row:
            return pos + self.margin[0]
        else:
            return pos + self.margin[1]

    def draw(self, background='white', color='blue', width=1, numbers=None,
             font_name='font.ttf', font_size=50):
        image = Image.new('RGBA', self.base_size, color=background)
        draw = ImageDraw.Draw(image)
        for cell in self.cells:
            corners = [getattr(cell, attr) for attr in CELL_ATTRS]
            draw.rectangle(tuple(corners), outline=color, width=width)
        if numbers:
            font = ImageFont.truetype(font_name, font_size)
            for pos, font_dict in numbers.items():
                value = font_dict['num']
                color = font_dict['color']
                text_size = draw.textsize(str(value), font=font)
                base = self._number_base(pos, text_size)
                draw.text(base, text=str(value), font=font, fill=color)
        return image

    def _number_base(self, pos, text_size):
        cell_corner = (self._fit_margin(pos[0] * self.cell_size[0], True),
                       self._fit_margin(pos[1] * self.cell_size[1], False))
        return (cell_corner[0] + (self.cell_size[0] - text_size[0]) / 2,
                cell_corner[1] + (self.cell_size[1] - text_size[1]) / 2)


Cell = namedtuple('Cell', list(CELL_ATTRS))


def cell_corners(cell):
    return (getattr(cell, 'x_min'))



if __name__ == '__main__': main()
