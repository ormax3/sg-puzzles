import io
import zipfile

def createZip(imgBytes, msg):
  # walk bytes (in reverse) recurisvely compressing content
  data = msg
  for idx, byte in enumerate(reversed(imgBytes)):
    # create zip file backed by in-memory buffer
    with io.BytesIO() as out:
      with zipfile.ZipFile(out, 'w', compression=zipfile.ZIP_DEFLATED, compresslevel=1) as z:
        # use current hex value as filename
        name = f"{byte:02X}.{'txt' if idx==0 else 'zip'}"
        # write data bytes
        z.writestr(name, data)
        # show progress
        print(f'{idx:04}: {name}')
      # get buffer bytes as payload for next iteration
      data = out.getvalue()
  return data

# read image bytes
with open(r'../png.png', 'rb') as f:
  imgBytes = f.read()

# create recursive zip
# given hint message for the innermost text file
msg = b'Its the not the destination, Its the journey.'
data = createZip(imgBytes, msg)

# write final payload to output zip
with open('png.zip', 'wb') as f:
  f.write(data)
