
class WindowedValue:

    def __init__(self, win_size):
        self.win_size = win_size
        self.values = []
        self.n = 0
        self.sum = 0
        self.avg = 0

    def add(self, val):
        if self.n < self.win_size:
            self.values.append(val)
            self.n += 1
            self.sum += val
            self.avg += ((val - self.avg) / (self.n + 1))
        else:
            self.sum += (val - self.values[0])
            self.avg += ((val / self.win_size) - (self.values[0] / self.win_size))
            self.values.pop(0)
            self.values.append(val)

    def get_avg(self):
        return self.avg

